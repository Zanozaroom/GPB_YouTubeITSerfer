package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.domain.repositories.EmptyNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class VideoListLoadImpl@Inject constructor(
    private val network: RepositoryNetwork,
    private val videoHelper: VideoLoad,
    @Named("Dispatchers.Default") val coroutineDispatcher: CoroutineDispatcher
): VideoListLoad {

    override suspend fun getVideoList(
        playListId: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTVideoListPaging> {
        val result = network.getListVideos(playListId, token, maxResult)
        return when (result) {
            ErrorNetworkResult -> ErrorLoadingViewModel
            is EmptyNetworkResult -> EmptyResultViewModel
            is SuccessNetworkResult -> {
                val finishResult = createYTVideoList(result.dataNetworkResult)
                when (finishResult) {
                    null -> EmptyResultViewModel
                    else -> SuccessViewModel(finishResult)
                }
            }
        }
    }

    override suspend fun getLoadMoreVideoList(
        playList: YTVideoListPaging,
        maxResult: Int
    ): ViewModelResult<YTVideoListPaging> {
        val result = network.getListVideos(playList.idPlayList, playList.nextToken, maxResult)
        return when (result) {
            is ErrorNetworkResult -> ErrorLoadingViewModel
            is EmptyNetworkResult -> NotMoreLoadingViewModel
            is SuccessNetworkResult -> {
                val finishResult = createYTVideoListPaging(playList, result.dataNetworkResult)
                when (finishResult) {
                    null -> NotMoreLoadingViewModel
                    else -> SuccessViewModel(finishResult)
                }
            }
        }
    }

    override suspend fun loadVideoFromYouTubeForSave(idVideo: String): YTVideo? {
        return videoHelper.loadingYTVideoForSaving(idVideo)
    }

    override suspend fun saveVideo(video: YTVideo) {
        videoHelper.saveVideo(video)
    }

    private suspend fun createYTVideoListPaging(
        oldData: YTVideoListPaging, newData: ModelLoadListVideosResponse
    ): YTVideoListPaging? {
        return withContext(coroutineDispatcher) {
            val filterList = filterYTVideoList(newData)
            if(filterList.isEmpty()) null
            else{
                val concatVideoList = mutableListOf<YTVideoList>()
                concatVideoList.addAll(oldData.listVideoList)
                concatVideoList.addAll(filterList)
                YTVideoListPaging(
                    idPlayList = oldData.idPlayList,
                    nextToken = newData.nextPageToken ?: NULL_DATA,
                    listVideoList = concatVideoList
                )
            }
        }
    }

    private suspend fun createYTVideoList(videoListResponse: ModelLoadListVideosResponse):YTVideoListPaging?{
        return  withContext(coroutineDispatcher){
            val filterList = filterYTVideoList(videoListResponse)
            if(filterList.isEmpty()) null
            else{
                val idPlayList = videoListResponse.contentConteiner!!.first().detailsVideoList!!.playlistId!!
                YTVideoListPaging(
                    idPlayList = idPlayList,
                    nextToken = videoListResponse.nextPageToken ?: NULL_DATA,
                    listVideoList = filterList)
            }
        }
    }

    private fun filterYTVideoList(response: ModelLoadListVideosResponse): List<YTVideoList>{
       return response.toVideoList()!!.filter { it.image.isNotBlank() }
    }

    companion object{
        private const val NULL_DATA = ""
    }

}