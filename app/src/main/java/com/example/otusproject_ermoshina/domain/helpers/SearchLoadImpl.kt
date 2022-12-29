package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.repositories.RepositoryDataBase
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad.Companion.ObjectYTSearch
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad.Companion.TOKEN_NULL
import com.example.otusproject_ermoshina.domain.model.YTQuery
import com.example.otusproject_ermoshina.domain.model.YTSearch
import com.example.otusproject_ermoshina.domain.model.YTSearchAndTitle
import javax.inject.Inject

class SearchLoadImp @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
) : SearchLoad {
    private val objectYTSearch = ObjectYTSearch()
    private var _listYTSearch = mutableListOf<YTSearch>()

    override suspend fun getResultSearch(query: String, token: String, maxResult: Int): List<YTSearch> {
        return getYTSearch(query,maxResult = maxResult, token = token)
    }

    override suspend fun getLoadMoreResultSearch(query: String, maxResult: Int): List<YTSearch>? {
        if (objectYTSearch.token.isBlank()) return null
        else return getYTSearch(query, maxResult, objectYTSearch.token)

    }
    /**
     * Формируем главную страницу
     */
    override suspend fun getResultSearchAndTitleFirstStart(maxResult: Int): List<YTSearchAndTitle> {
        val listQuery = getQueryAppRoom()
        val list = listQuery.map {
            getYTSearchAndTitle(it, token = TOKEN_NULL, maxResult = maxResult)
        }
        return list
    }
    /**
     * Приватный метод для формирования ответа: Вопрос[Массив видео].
     * Получаем на входе один запрос для поиска
     * Внутри вызывается метод getNetworkDataQuery() для вопроса, переданного в списке
     */
    private suspend fun getYTSearchAndTitle(query: YTQuery, maxResult: Int, token: String, safeSearch:String = PART_SEARCH_SAFE): YTSearchAndTitle {
        val result = getYTSearchFirstStart(query.query,2)
        return YTSearchAndTitle(
            id = result.hashCode(),
            query = query.query,
            title = query.titleQuery,
            listResult = result
        )
    }

    private suspend fun getYTSearchFirstStart(query: String,maxResult: Int): List<YTSearch> {
        return network.getResultSearch(query,maxResult, TOKEN_NULL,null).toYTSearch()
    }
    /**
     * Приватный метод для поиска по ютубу. Добавляет в сохраненный внутренний массив _listYTSearch
     * результаты поиска. После возвращает обновленный массив видео по поиску. Этот массив для пагинации
     */
    private suspend fun getYTSearch(query: String, maxResult: Int, token: String, safeSearch:String = PART_SEARCH_SAFE): List<YTSearch> {
        val searchList = network.getResultSearch(query,maxResult, token,safeSearch)
        objectYTSearch.token = searchList.nextPageToken ?: TOKEN_NULL
        val result = searchList.toYTSearch()
        _listYTSearch = ArrayList(_listYTSearch)
        _listYTSearch.addAll(result)
        objectYTSearch.listYTSearch = _listYTSearch
        return objectYTSearch.listYTSearch
    }



    /**
     * Вызываем для получение всех вопросов из базы данных
     */
    private suspend fun getQueryAppRoom(): List<YTQuery> {
            return dataBase.loadQuery().map { it.toYTQuery() }
    }

companion object{
    const val PART_SEARCH_SAFE = "strict"
}
}