package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.helpers.VideoListLoad.Companion.ObjectYTVideoList
import com.example.otusproject_ermoshina.domain.helpers.VideoListLoad.Companion.TOKEN_NULL
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class VideoListLoadImpl@Inject constructor(
    private val network: RepositoryNetwork,
    private val videoHelper: VideoLoad
): VideoListLoad {
    private var objectYTVideoList = ObjectYTVideoList()
    private var _listYTVideoListByPlayList = mutableListOf<YTVideoList>()

    override suspend fun getVideoList(
        playListId: String,
        token: String,
        maxResult:Int
    ): List<YTVideoList>? {
        val result = network.getListVideos(playListId, token, maxResult)
        if (result?.items.isNullOrEmpty()) return null
        objectYTVideoList.token = result?.nextPageToken ?: TOKEN_NULL

        _listYTVideoListByPlayList = ArrayList(_listYTVideoListByPlayList)
        _listYTVideoListByPlayList.addAll(result?.toVideoList()!!)

        objectYTVideoList.listYTVideoList = _listYTVideoListByPlayList

        return objectYTVideoList.listYTVideoList
    }

    override suspend fun getLoadMoreVideoList(
        playListId: String,
        maxResult: Int
    ): List<YTVideoList>? {
        if (objectYTVideoList.token.isBlank()) return null
        else return getVideoList(playListId, objectYTVideoList.token, maxResult)
    }

    override suspend fun loadVideo(idVideo: String):YTVideo {
        return network.loadOneVideo(idVideo).toVideo()
    }

    override suspend fun saveVideo(video: YTVideo) {
        videoHelper.saveVideo(video)
    }

}