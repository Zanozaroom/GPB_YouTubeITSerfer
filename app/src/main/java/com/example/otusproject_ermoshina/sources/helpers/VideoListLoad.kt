package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.base.YTVideoList

interface VideoListLoad {
    suspend fun getVideoList(
        playListId: String,
        token: String = TOKEN_NULL,
        maxResult:Int = MAXRESULT
    ): List<YTVideoList>?

    suspend fun getLoadMoreVideoList(playListId: String, maxResult:Int = MAXRESULT_LOADMORE): List<YTVideoList>?

    suspend fun saveVideo(video:YTVideo)

    companion object{
        const val MAXRESULT = 6
        const val MAXRESULT_LOADMORE = 4
        const val TOKEN_NULL = ""

        data class ObjectYTVideoList(
            var token: String = TOKEN_NULL,
            var listYTVideoList: List<YTVideoList> = emptyList()
        )
    }
}
