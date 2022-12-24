package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.base.YTVideoList

interface VideoListLoad {
    suspend fun getListVideoByIdPlayList(
        playListId: String,
        token: String = BaseHelper.TOKEN_NULL
    ): List<YTVideoList>?

    suspend fun getLoadMoreVideoList(playListId: String): List<YTVideoList>?

}