package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.servise.retrofit.model.ChannelPlayListResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideoResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearchResponse

interface RepositoryNetwork {
    suspend fun loadChannelList(
        channelId: String,
        token: String,
        maxResult: Int
    ): NetworkResult<ChannelPlayListResponse>

    suspend fun getListVideos(
        playListId: String,
        token: String,
        maxResult: Int
    ): NetworkResult<ModelLoadListVideosResponse>

    suspend fun loadOneVideo(idVideo: String): NetworkResult<ModelLoadVideoResponse>
    suspend fun getResultSearch(
        query: String,
        maxResult: Int,
        token: String,
        safeSearch: String?
    ): NetworkResult<ModelSearchResponse>
}
