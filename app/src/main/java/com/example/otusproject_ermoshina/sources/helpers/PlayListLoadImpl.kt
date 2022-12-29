package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.RepositoryNetwork
import com.example.otusproject_ermoshina.sources.helpers.PlayListLoad.Companion.MAXRESULT_LOADMORE
import com.example.otusproject_ermoshina.sources.helpers.PlayListLoad.Companion.objectYTPlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayListLoadImpl @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
) : PlayListLoad {
    private var playlistResult = objectYTPlayList()
    private var _listPlayListOneChannel = mutableListOf<YTPlayList>()

    override suspend fun getOneChannelAndVideoList(
        channelId: String,
        token: String,
        maxResult: Int
    ): List<YTPlayList> {
        val result = network.loadChannelList(channelId, token,maxResult)
        playlistResult.token = result.nextToken
        _listPlayListOneChannel = ArrayList(_listPlayListOneChannel)
        _listPlayListOneChannel.addAll(result.toVideosChannel())
        playlistResult.listPlayList = _listPlayListOneChannel
        return playlistResult.listPlayList
    }
    override suspend fun getLoadMoreListOfChannel(channelId: String): List<YTPlayList>? {
        if (playlistResult.token.isBlank()) return null
        else return getOneChannelAndVideoList(channelId, playlistResult.token,MAXRESULT_LOADMORE)
    }

    override suspend fun addToFavoritePlayList(playlist: YTPlayList) {
        dataBase.addPlayListToFavorite(playlist)
    }

    override fun loadFavoritePlayList(): Flow<List<YTPlayList>> {
        return  dataBase.loadPlayList().map { listRoomPlayList ->
            listRoomPlayList.map { it.toYTPlayList() }
        }
    }

    override suspend fun deletePlayListFromFavorite(playList: YTPlayList) {
        dataBase.deletePlayList(playList)
    }

}


