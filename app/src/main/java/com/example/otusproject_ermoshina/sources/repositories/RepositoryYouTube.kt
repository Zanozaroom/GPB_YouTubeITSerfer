package com.example.otusproject_ermoshina.sources.repositories

import com.example.otusproject_ermoshina.base.*
import com.example.otusproject_ermoshina.sources.RepositoryNetwork
import com.example.otusproject_ermoshina.sources.retrofit.YTApi
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadListVideos
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadVideo
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelSearch
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryYouTube @Inject constructor(
    private val retrofit: YTApi
): RepositoryNetwork {

    override suspend fun loadChannelList(
        channelId: String,
        token: String,
        maxResult: Int,
    ): YTChannelAndPlayList =
        withContext(Dispatchers.IO) {
            val response =
                retrofit.getListChannels(PART_CHANNEL, maxResult, token, channelId, KEY)
            if (response.isSuccessful) {
                response.body()!!.toChannelAndListVideos()
            } else {
                throw NetworkLoadException("RetrofitAbsoluteLoadException" + response.code())
            }
        }

    override suspend fun getListVideos(playListId: String, token: String, maxResult: Int): ModelLoadListVideos? =
        withContext(Dispatchers.IO) {
            val response = retrofit.getListVideos(PART_VIDEO_LIST, token, maxResult, playListId,KEY)
            if (response.isSuccessful) {
               response.body()
            } else {
                throw NetworkLoadException("RetrofitAbsoluteLoadException " +response.code().toString())
            }
        }

    override suspend fun loadOneVideo(idVideo: String): ModelLoadVideo =
        withContext(Dispatchers.IO) {
            val response =  retrofit.getOneVideo(PART_ONE_VIDEO, idVideo, KEY)
            if(response.isSuccessful){
                response.body()!!
            } else{
                throw NetworkLoadException("RetrofitAbsoluteLoadException " +response.code().toString())
            }
        }

    override suspend fun getResultSearch(query: String, maxResult: Int, token: String, safeSearch:String?): ModelSearch =
        withContext(Dispatchers.IO) {
            val response =  retrofit.getResultSearch(PART_SEARCH, maxResult,token,query, KEY, safeSearch?:PART_SEARCH_SAFE)
            if(response.isSuccessful){
                response.body()!!
            } else{
                throw NetworkLoadException("RetrofitAbsoluteLoadException " +response.code().toString())
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