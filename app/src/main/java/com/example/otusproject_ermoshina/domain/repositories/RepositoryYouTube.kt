package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.servise.retrofit.YTApi
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideoResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearchResponse
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RepositoryYouTube @Inject constructor(
    private val retrofit: YTApi,
    @Named("Dispatchers.IO") private val dispatcher: CoroutineDispatcher
) : RepositoryNetwork {

    override suspend fun loadChannelList(
        channelId: String,
        token: String,
        maxResult: Int,
    ): NetworkResult<YTPlayListPaging> =
        withContext(dispatcher) {
            val response =
                retrofit.getListChannels(PART_CHANNEL, maxResult, token, channelId, KEY)
            if (response.isSuccessful) {
                SuccessNetworkResult(response.body()!!.toChannelAndListVideos())
            } else {
                ErrorNetworkResult
            }
        }

    override suspend fun getListVideos(
        playListId: String, token: String, maxResult: Int
    ): NetworkResult<ModelLoadListVideosResponse> =
        withContext(dispatcher) {
            val response = retrofit.getListVideos(
                PART_VIDEO_LIST, token, maxResult, playListId, KEY
            )

            if (response.isSuccessful) {
                SuccessNetworkResult(response.body()!!)
            } else {
                ErrorNetworkResult
            }
        }

    override suspend fun loadOneVideo(idVideo: String): NetworkResult<ModelLoadVideoResponse> =
        withContext(dispatcher) {
            val response = retrofit.getOneVideo(PART_ONE_VIDEO, idVideo, KEY)
            if (response.isSuccessful) {
                SuccessNetworkResult(response.body()!!)
            } else {
                ErrorNetworkResult
            }
        }

    override suspend fun getResultSearch(
        query: String,
        maxResult: Int,
        token: String,
        safeSearch: String?
    ): NetworkResult<ModelSearchResponse> =
        withContext(dispatcher) {
            val response = retrofit.getResultSearch(
                PART_SEARCH,
                maxResult,
                token,
                query,
                KEY,
                safeSearch ?: PART_SEARCH_SAFE
            )
            if (response.isSuccessful) {
                SuccessNetworkResult(response.body()!!)
            } else {
                ErrorNetworkResult
            }
        }

    companion object {
        const val KEY = "AIzaSyDAgcGjXUNHjjNC4zQgpIgL16Wm2HNxr1I"
        const val PART_CHANNEL = "snippet"
        const val PART_VIDEO_LIST = "snippet,ContentDetails"
        const val PART_ONE_VIDEO = "snippet,statistics"
        const val PART_SEARCH = "snippet"
        const val PART_SEARCH_SAFE = "strict"

    }


}