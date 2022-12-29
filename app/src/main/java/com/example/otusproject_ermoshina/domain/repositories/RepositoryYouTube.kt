package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.model.YTChannelAndPlayList
import com.example.otusproject_ermoshina.servise.retrofit.YTApi
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideos
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideo
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearch
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
sealed class NetworkResult<out T>
data class SuccessNetworkResult<out R>(val dataNetworkResult: R) : NetworkResult<R>()
object ErrorNetworkResult : NetworkResult<Nothing>()

@Singleton
class RepositoryYouTube @Inject constructor(
    private val retrofit: YTApi
): RepositoryNetwork {

    override suspend fun loadChannelList(
        channelId: String,
        token: String,
        maxResult: Int,
    ): NetworkResult<YTChannelAndPlayList> =
        withContext(Dispatchers.IO) {
            val response =
                retrofit.getListChannels(PART_CHANNEL, maxResult, token, channelId, KEY)
            if (response.isSuccessful) {
                SuccessNetworkResult(response.body()!!.toChannelAndListVideos())
            } else {
                ErrorNetworkResult
            }
        }

    override suspend fun getListVideos(playListId: String, token: String, maxResult: Int): ModelLoadListVideos? =
        withContext(Dispatchers.IO) {
            val response = retrofit.getListVideos(
                PART_VIDEO_LIST, token, maxResult, playListId,
                KEY
            )
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
            val response =  retrofit.getResultSearch(PART_SEARCH, maxResult,token,query, KEY, safeSearch?: PART_SEARCH_SAFE)
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