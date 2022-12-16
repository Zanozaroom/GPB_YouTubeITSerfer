package com.example.otusproject_ermoshina.sources.retrofit

import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadListVideos
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadVideo
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelChannelPlayList
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseYouTubeHelper @Inject constructor(private val retrofit: YTApi) {

    suspend fun getOneVideo(
        part: String,
        id: String,
        key: String
    ): Response<ModelLoadVideo> {
        return retrofit.getOneVideo(part, id, key)
    }

    suspend fun getListVideos(
        part: String,
        maxRes: Int,
        playlistId: String,
        key: String
    ): Response<ModelLoadListVideos> {
        return retrofit.getListVideos(part, maxRes, playlistId, key)
    }

    suspend fun getListChannels(
        part: String,
        maxRes: Int,
        nextToken: String,
        channelId: String,
        key: String
    ): Response<ModelChannelPlayList> {
        return retrofit.getListChannels(part, maxRes, nextToken, channelId, key)
    }


}