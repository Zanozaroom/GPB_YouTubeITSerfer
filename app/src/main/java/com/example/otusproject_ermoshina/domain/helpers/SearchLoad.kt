package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTMainFragmentData
import com.example.otusproject_ermoshina.domain.model.YTSearchPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult

interface SearchLoad {
    suspend fun getLoadMoreResultSearch(
        ytSearchPaging: YTSearchPaging,
        maxResult: Int
    ): ViewModelResult<YTSearchPaging>

    suspend fun getResultSearch(
        query: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTSearchPaging>

    suspend fun getMainFragmentPage(maxResult: Int): ViewModelResult<List<YTMainFragmentData>>

}
