package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.repositories.RepositoryDataBase
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.model.*
import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearchResponse
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SearchLoadImpl @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase,
    @Named("Dispatchers.Default") val coroutineDispatcher: CoroutineDispatcher
) : SearchLoad {

    override suspend fun getResultSearch(
        query: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTSearchPaging> {
        val searchNetworkResult = network.getResultSearch(query, maxResult, token, PART_SEARCH_SAFE)
        return when (searchNetworkResult) {
            is ErrorNetworkResult -> ErrorLoadingViewModel
            is SuccessNetworkResult -> {
                if (searchNetworkResult.dataNetworkResult.resultSearchAllData.isNullOrEmpty()) {
                    EmptyResultViewModel
                } else {
                    val ytSearchPaging =
                        createYTSearchPaging(searchNetworkResult.dataNetworkResult, query)
                    SuccessViewModel(ytSearchPaging)
                }

            }
        }
    }

    override suspend fun getLoadMoreResultSearch(
        ytSearchPaging: YTSearchPaging,
        maxResult: Int
    ): ViewModelResult<YTSearchPaging> {
        val searchNetworkResult = network.getResultSearch(
            ytSearchPaging.query,
            maxResult,
            ytSearchPaging.nextToken,
            PART_SEARCH_SAFE
        )
        return when (searchNetworkResult) {
            is ErrorNetworkResult -> ErrorLoadingViewModel
            is SuccessNetworkResult -> {
                val newYTSearchPaging =
                    createLoadMoreYTSearchPaging(
                        ytSearchPaging,
                        searchNetworkResult.dataNetworkResult
                    )
                SuccessViewModel(newYTSearchPaging)
            }
        }
    }
    /**
     * Формирует главную страницу
     */
    override suspend fun getMainFragmentPage(maxResult: Int): ViewModelResult<List<YTMainFragmentData>> {
        val concatResponse = mutableListOf<YTMainFragmentData>()
        val listQuery = getQueryFromDataBase()

        listQuery.forEach { ytQuery ->
            val searchNetworkResult = network.getResultSearch(
                ytQuery.query, maxResult, TOKEN_NULL, PART_SEARCH_SAFE)

            when (searchNetworkResult) {
                is ErrorNetworkResult -> {}
                is SuccessNetworkResult -> {
                    if(!searchNetworkResult.dataNetworkResult.resultSearchAllData.isNullOrEmpty()){
                        val ytMainFragmentData =
                            createYTMainFragmentData(ytQuery, searchNetworkResult.dataNetworkResult)
                        concatResponse.add(ytMainFragmentData)
                    }
                }
            }
        }
        return if (concatResponse.isEmpty()) ErrorLoadingViewModel
        else SuccessViewModel(concatResponse)
    }


    private suspend fun getQueryFromDataBase(): List<YTQuery> {
        return dataBase.loadQuery().map { it.toYTQuery() }
    }

    private suspend fun createLoadMoreYTSearchPaging(
        oldData: YTSearchPaging,
        newData: ModelSearchResponse
    ): YTSearchPaging = withContext(coroutineDispatcher) {
        val concatYTSearch = mutableListOf<YTSearch>()
        concatYTSearch.addAll(oldData.listSearchVideo)
        concatYTSearch.addAll(newData.toYTSearch())
        YTSearchPaging(
            query = oldData.query,
            nextToken = newData.nextPageToken ?: "",
            listSearchVideo = concatYTSearch
        )
    }

    private suspend fun createYTSearchPaging(getResponse: ModelSearchResponse, query: String) =
        withContext(coroutineDispatcher) {
            YTSearchPaging(
                query = query,
                nextToken = getResponse.nextPageToken ?: TOKEN_NULL,
                listSearchVideo = getResponse.toYTSearch()
            )
        }

    private suspend fun createYTMainFragmentData(query: YTQuery, searchResponse: ModelSearchResponse)
            : YTMainFragmentData = withContext(coroutineDispatcher) {
        YTMainFragmentData(
            id = searchResponse.hashCode(),
            query = query.query,
            title = query.titleQuery,
            listResult = searchResponse.toYTSearch()
        )
    }

    companion object {
        const val PART_SEARCH_SAFE = "strict"
        const val TOKEN_NULL = ""
    }
}