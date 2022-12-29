package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTSearch
import com.example.otusproject_ermoshina.base.YTSearchAndTitle

interface SearchLoad {
    suspend fun getLoadMoreResultSearch(query: String, maxResult: Int = MAXRESULT_LOADMORE): List<YTSearch>?
    suspend fun getResultSearch(query: String,
                                token: String = TOKEN_NULL,
                                maxResult: Int = MAXRESULT): List<YTSearch>
    suspend fun getResultSearchAndTitleFirstStart(maxResult: Int = 2): List<YTSearchAndTitle>

companion object{
        const val MAXRESULT = 6
        const val MAXRESULT_LOADMORE = 3
        const val TOKEN_NULL = ""

        data class ObjectYTSearch(
            var token: String = TOKEN_NULL,
            var listYTSearch: List<YTSearch> = emptyList()
        )
    }
}

