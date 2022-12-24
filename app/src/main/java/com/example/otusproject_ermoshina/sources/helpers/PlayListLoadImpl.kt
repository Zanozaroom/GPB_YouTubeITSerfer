package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTChannelAndListVideos
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.RepositoryNetwork
import com.example.otusproject_ermoshina.sources.helpers.BaseHelper.Companion.SafeToken
import javax.inject.Inject

class PlayListLoadImpl @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
) : PlayListLoad, BaseHelper() {
    private var tokenPlayList = SafeToken.TokenPlayList()
    private var _listPlayListOneChannel = mutableListOf<YTPlayListOfChannel>()


    override suspend fun getOneChannelAndVideoList(
        channelId: String,
        token: String
    ): List<YTPlayListOfChannel> {
        val result = network.loadChannelList(channelId, token)
        tokenPlayList.token = result.nextToken
        _listPlayListOneChannel = ArrayList(_listPlayListOneChannel)
        _listPlayListOneChannel.addAll(result.toVideosChannel())
        return _listPlayListOneChannel
    }
    override suspend fun getLoadMoreListOfChannel(channelId: String): List<YTPlayListOfChannel>? {
        if (tokenPlayList.token.isBlank()) return null
        else return getOneChannelAndVideoList(channelId, tokenPlayList.token)
    }

    override suspend fun addToFavoritePlayList(playlist: YTPlayListOfChannel) {
        dataBase.addPlayListToFavorite(playlist)
    }


    fun toChannelForLoadInRoom(listVideos: List<YTPlayListOfChannel>):YTChannelAndListVideos {
        val idChannel = listVideos.first().idChannel
        val titleChannel = listVideos.first().titleChannel
        return  YTChannelAndListVideos(
            idChannel = idChannel,
            titleChannel = titleChannel,
            nextToken = "",
            listVideos = listVideos
        )
    }
    }


