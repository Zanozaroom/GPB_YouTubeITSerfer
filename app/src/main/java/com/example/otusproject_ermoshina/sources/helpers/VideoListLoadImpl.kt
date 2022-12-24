package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTVideoList
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.RepositoryNetwork
import com.example.otusproject_ermoshina.sources.helpers.BaseHelper.Companion.SafeToken.TokenVideoListByPlayList
import javax.inject.Inject

class VideoListLoadImpl@Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
):VideoListLoad, BaseHelper() {
    private var tokenVideoListByPlayList = TokenVideoListByPlayList()
    private var _listYTVideoListByPlayList = mutableListOf<YTVideoList>()

    override suspend fun getListVideoByIdPlayList(
        playListId: String,
        token: String
    ): List<YTVideoList>? {
        val result = network.getListVideos(playListId, token)
        if (result?.items.isNullOrEmpty()) return null
        tokenVideoListByPlayList.token = result?.nextPageToken ?: TOKEN_NULL
        _listYTVideoListByPlayList = ArrayList(_listYTVideoListByPlayList)
        _listYTVideoListByPlayList.addAll(result?.toVideoList()!!)
        return _listYTVideoListByPlayList
    }

    override suspend fun getLoadMoreVideoList(playListId: String): List<YTVideoList>? {
        if (tokenVideoListByPlayList.token.isBlank()) return null
        else return getListVideoByIdPlayList(playListId, tokenVideoListByPlayList.token)
    }

}