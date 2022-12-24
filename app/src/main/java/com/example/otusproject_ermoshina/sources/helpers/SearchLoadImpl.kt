package com.example.otusproject_ermoshina.sources.helpers

import android.util.Log
import com.example.otusproject_ermoshina.base.*
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.RepositoryNetwork
import com.example.otusproject_ermoshina.sources.helpers.BaseHelper.Companion.SafeToken.TokenSearchMain
import com.example.otusproject_ermoshina.sources.helpers.BaseHelper.Companion.SafeToken.TokenSearchResultVideoWithTitle

import javax.inject.Inject

class SearchLoadImp @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
) : SearchLoad, BaseHelper() {
    private var tokenSearchMain = TokenSearchMain()
    private val _tokenSearchVideoWithTitle = TokenSearchResultVideoWithTitle()

    /**
     * Переменные для внутреннего хранения списка с результатами поска
     * _listYTSearch - хранит список вида [массив видео]
     * _listResultVideoAndTitleQuestion - хранит список вида - Вопрос[массив видео]
     */

    private var _listYTSearch = mutableListOf<YTSearch>()
    private var _listResultVideoAndTitleQuestion = mutableListOf<YTSearchAndTitle>()

    override suspend fun getResultSearch(query: String, token: String, maxResult: Int): List<YTSearch> {
        return getYTSearch(query,maxResult = maxResult, token = token)
    }

    override suspend fun getLoadMoreResultSearch(query: String, maxResult: Int): List<YTSearch>? {
        if (tokenSearchMain.token.isBlank()) return null
        else return getResultSearch(query, token = tokenSearchMain.token, maxResult = maxResult)
    }



    /**
     * Формируем главную страницу
     */
    override suspend fun getResultSearchAndTitleFirstStart(): List<YTSearchAndTitle> {
        val resultList = mutableListOf<YTSearchAndTitle>()
        val listQuery = getQueryAppRoom()
        listQuery.forEach {
            resultList.add(getYTSearchAndTitle(it, token =TOKEN_NULL))
        }
        return resultList
    }

    private suspend fun getYTSearchFirstStart(query: String): List<YTSearch> {
        return network.getResultSearch(query,3, TOKEN_NULL,null).toYTSearch()
    }





    private suspend fun getYTSearchAndTitleFirstStart(query: YTQuery): YTSearchAndTitle {
        val result = getYTSearchFirstStart(query.query)
        return YTSearchAndTitle(
            id = result.hashCode(),
            query =query.query,
            title = query.titleQuery,
            listResult = result
        )
    }



    /**
     * Приватный метод для поиска по ютубу. Добавляет в сохраненный внутренний массив _listYTSearch
     * результаты поиска. После возвращает обновленный массив видео по поиску. Этот массив
     */
    private suspend fun getYTSearch(query: String, maxResult: Int = MAXRESULT, token: String, safeSearch:String = PART_SEARCH_SAFE): List<YTSearch> {
        val searchlist = network.getResultSearch(query,maxResult, token,safeSearch)
        tokenSearchMain.token = searchlist.nextPageToken ?: TOKEN_NULL
        _listYTSearch = ArrayList()
        _listYTSearch.addAll(searchlist.toYTSearch())
        return ArrayList(_listYTSearch)
    }

    /**
     * Приватный метод для формирования ответа: Вопрос[Массив видео].
     * Получаем на входе один запрос для поиска
     * Внутри вызывается метод getNetworkDataQuery() для вопроса, переданного в списке
     */
    private suspend fun getYTSearchAndTitle(query: YTQuery, maxResult: Int = MAXRESULT, token: String, safeSearch:String = PART_SEARCH_SAFE): YTSearchAndTitle {
        val result = getYTSearch(query.query,maxResult, token,safeSearch)
        return YTSearchAndTitle(
            id = result.hashCode(),
            query = query.query,
            title = query.titleQuery,
            listResult = result
        )
    }

    /**
     * Вызываем для получение всех вопросов из базы данных
     */
    private suspend fun getQueryAppRoom(): List<YTQuery> {
        try {
            return dataBase.loadQuery().map { it.toYTQuery() }
        } catch (e: Exception) {
            throw DataBaseLoadException(e.toString())
        }
    }

companion object{
    const val PART_SEARCH_SAFE = "safeSearch"


    const val MAXRESULT = 3
}
}