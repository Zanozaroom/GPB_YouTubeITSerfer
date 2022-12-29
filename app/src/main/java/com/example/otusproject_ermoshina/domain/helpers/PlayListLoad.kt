package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTChannelAndPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import kotlinx.coroutines.flow.Flow

interface PlayListLoad {
    suspend fun loadChannelAndPlayList(
        channelId: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTChannelAndPlayList>

    suspend fun addToFavoritePlayList(playlist: YTPlayList)
    fun loadFavoritePlayList(): Flow<List<YTPlayList>>
    suspend fun deletePlayListFromFavorite(idPlayList: String)

}