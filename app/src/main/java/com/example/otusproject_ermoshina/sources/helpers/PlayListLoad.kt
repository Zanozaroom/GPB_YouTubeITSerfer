package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTChannelAndListVideos
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel

interface PlayListLoad {
    suspend fun getOneChannelAndVideoList(
        channelId: String,
        token: String = BaseHelper.TOKEN_NULL
    ): List<YTPlayListOfChannel>

    suspend fun getLoadMoreListOfChannel(channelId: String): List<YTPlayListOfChannel>?
    suspend fun addToFavoritePlayList(playlist:YTPlayListOfChannel)
}