package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import kotlinx.coroutines.flow.Flow

interface PlayListLoad {
    suspend fun firstLoadPlayList(
        idChannel: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTPlayListPaging>

    suspend fun loadMorePlayList(
        channel: YTPlayListPaging,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTPlayListPaging>

    suspend fun addToFavoritePlayList(playlist: YTPlayList)
    fun loadFavoritePlayList(): Flow<List<YTPlayList>>
    suspend fun deletePlayListFromFavorite(idPlayList: String)

}