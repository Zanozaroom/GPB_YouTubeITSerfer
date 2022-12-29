package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.model.YTChannelAndPlayList
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideos
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideo
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearch

interface RepositoryNetwork {
    suspend fun loadChannelList(
        channelId: String,
        token: String,
        maxResult: Int
    ): NetworkResult<YTChannelAndPlayList>

    suspend fun getListVideos(
        playListId: String,
        token: String,
        maxResult: Int
    ): ModelLoadListVideos?

    suspend fun loadOneVideo(idVideo: String): ModelLoadVideo
    suspend fun getResultSearch(
        query: String,
        maxResult: Int,
        token: String,
        safeSearch: String?
    ): ModelSearch
}
