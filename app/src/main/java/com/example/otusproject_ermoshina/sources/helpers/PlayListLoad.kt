package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTPlayList
import kotlinx.coroutines.flow.Flow

interface PlayListLoad {
    suspend fun getOneChannelAndVideoList(
        channelId: String,
        token: String = TOKEN_NULL,
        maxResult: Int = MAXRESULT
    ): List<YTPlayList>

    suspend fun getLoadMoreListOfChannel(channelId: String): List<YTPlayList>?

    suspend fun addToFavoritePlayList(playlist: YTPlayList)
    fun loadFavoritePlayList(): Flow<List<YTPlayList>>
    suspend fun deletePlayListFromFavorite(playList: YTPlayList)

    companion object {
        const val MAXRESULT = 6
        const val MAXRESULT_LOADMORE = 4
        const val TOKEN_NULL = ""

        data class objectYTPlayList(
            var token: String = TOKEN_NULL,
            var listPlayList: List<YTPlayList> = emptyList()
        )
    }
}