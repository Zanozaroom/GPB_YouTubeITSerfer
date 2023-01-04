package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
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
            is SuccessNetworkResult -> {
                when {
                    result.dataNetworkResult.contentConteiner.isNullOrEmpty() -> EmptyResultViewModel
                    else -> SuccessViewModel(createYTVideoList(result.dataNetworkResult))
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
            ErrorNetworkResult -> ErrorLoadingViewModel
            is SuccessNetworkResult -> {
                when {
                    result.dataNetworkResult.contentConteiner.isNullOrEmpty() -> NotMoreLoadingViewModel
                    else -> {
                        val newYTVideoListPaging = createYTVideoListPaging(playList, result.dataNetworkResult)
                        SuccessViewModel(newYTVideoListPaging)
                    }
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
    ): YTVideoListPaging {
        return withContext(coroutineDispatcher) {
            val concatVideoList = mutableListOf<YTVideoList>()
            concatVideoList.addAll(oldData.listVideoList)
            concatVideoList.addAll(newData.toVideoList()!!)
            YTVideoListPaging(
                idPlayList = oldData.idPlayList,
                nextToken = newData.nextPageToken ?: NULL_DATA,
                listVideoList = concatVideoList
            )
        }
    }

    private suspend fun createYTVideoList(videoListResponse: ModelLoadListVideosResponse):YTVideoListPaging{
        return  withContext(coroutineDispatcher){
            val idPlayList = videoListResponse.contentConteiner!!.first().detailsVideoList!!.playlistId!!
            YTVideoListPaging(
                idPlayList = idPlayList,
                nextToken = videoListResponse.nextPageToken ?: NULL_DATA,
                listVideoList = videoListResponse.toVideoList()!!)
        }
    }
    companion object{
        private const val NULL_DATA = ""
    }

}