package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTSearch
import com.example.otusproject_ermoshina.base.YTSearchAndTitle

interface SearchLoad {
    suspend fun getLoadMoreResultSearch(query: String, maxResult: Int = 3): List<YTSearch>?
    suspend fun getResultSearch(query: String, token: String = BaseHelper.TOKEN_NULL,maxResult: Int): List<YTSearch>
    suspend fun getResultSearchAndTitleFirstStart(): List<YTSearchAndTitle>
}