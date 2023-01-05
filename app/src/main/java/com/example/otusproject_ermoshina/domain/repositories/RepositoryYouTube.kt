package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.servise.retrofit.YTApi
import com.example.otusproject_ermoshina.servise.retrofit.model.ChannelPlayListResponse
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
    ): NetworkResult<ChannelPlayListResponse> =
        withContext(dispatcher) {
            try {
                val response =
                    retrofit.getListChannels(PART_CHANNEL, maxResult, token, channelId, KEY)
                if (response.isSuccessful) {
                    when {
                        response.body() == null -> EmptyNetworkResult
                        else -> SuccessNetworkResult(response.body()!!)
                    }
                } else  ErrorNetworkResult
            } catch (e: Exception) {
                throw NetworkLoadException("нет подключения к серверу $e")
            }
        }



    override suspend fun getListVideos(
        playListId: String, token: String, maxResult: Int
    ): NetworkResult<ModelLoadListVideosResponse> =
        withContext(dispatcher) {
            try {
                val response = retrofit.getListVideos(
                    PART_VIDEO_LIST, token, maxResult, playListId, KEY
                )
                if (response.isSuccessful) {
                    when {
                        response.body() == null -> EmptyNetworkResult
                        else -> SuccessNetworkResult(response.body()!!)
                    }
                } else {
                    ErrorNetworkResult
                }
            } catch (e: Exception) {
                throw NetworkLoadException("нет подключения к серверу $e")
            }
        }

    override suspend fun loadOneVideo(idVideo: String): NetworkResult<ModelLoadVideoResponse> =
        withContext(dispatcher) {
            try {
                val response = retrofit.getOneVideo(PART_ONE_VIDEO, idVideo, KEY)
                if (response.isSuccessful) {
                    when {
                        response.body() == null -> EmptyNetworkResult
                        else -> SuccessNetworkResult(response.body()!!)
                    }
                } else {
                    ErrorNetworkResult
                }
            } catch (e: Exception) {
                throw NetworkLoadException("нет подключения к серверу $e")
            }
        }

    override suspend fun getResultSearch(
        query: String,
        maxResult: Int,
        token: String,
        safeSearch: String?
    ): NetworkResult<ModelSearchResponse> =
        withContext(dispatcher) {
            try {
                val response = retrofit.getResultSearch(
                    PART_SEARCH,
                    maxResult,
                    token,
                    query,
                    KEY,
                    safeSearch ?: PART_SEARCH_SAFE
                )
                if (response.isSuccessful) {
                    when {
                        response.body() == null -> EmptyNetworkResult
                        else -> SuccessNetworkResult(response.body()!!)
                    }
                } else {
                    ErrorNetworkResult
                }
            } catch (e: Exception) {
                throw NetworkLoadException("нет подключения к серверу $e")
            }

        }

    companion object {
        const val KEY = "AIzaSyCi4u78_AT3dcVbonADzVCJLq1__P5_FeI"
        const val PART_CHANNEL = "snippet"
        const val PART_VIDEO_LIST = "snippet,ContentDetails"
        const val PART_ONE_VIDEO = "snippet,statistics"
        const val PART_SEARCH = "snippet"
        const val PART_SEARCH_SAFE = "strict"

    }


}