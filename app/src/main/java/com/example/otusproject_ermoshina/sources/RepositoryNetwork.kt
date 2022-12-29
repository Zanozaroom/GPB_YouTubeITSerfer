package com.example.otusproject_ermoshina.sources

import com.example.otusproject_ermoshina.base.YTChannelAndPlayList
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadListVideos
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadVideo
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelSearch

interface RepositoryNetwork {
    suspend fun loadChannelList(channelId: String, token: String, maxResult: Int): YTChannelAndPlayList
    suspend fun getListVideos(
       playListId: String,
       token: String,
       maxResult: Int
    ): ModelLoadListVideos?
    suspend fun loadOneVideo(idVideo: String): ModelLoadVideo
    suspend fun getResultSearch(query: String, maxResult: Int, token: String, safeSearch:String?):ModelSearch
}
