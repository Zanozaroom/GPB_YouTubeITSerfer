package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTChannelAndPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.RepositoryDataBase
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayListLoadImpl @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
) : PlayListLoad {

    override suspend fun loadChannelAndPlayList(
        channelId: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTChannelAndPlayList> {
        val result = network.loadChannelList(channelId, token,maxResult)
        return when(result){
            is ErrorNetworkResult -> ErrorLoadingViewModel
            is SuccessNetworkResult -> {
                if(result.dataNetworkResult.listVideos.isEmpty()) return EmptyResultViewModel
                else return SuccessViewModel(result.dataNetworkResult)
            }
        }
    }

    override suspend fun addToFavoritePlayList(playlist: YTPlayList) {
        dataBase.addPlayListToFavorite(playlist)
    }

    override fun loadFavoritePlayList(): Flow<List<YTPlayList>> {
        return  dataBase.loadPlayList().map { listRoomPlayList ->
            listRoomPlayList.map { it.toYTPlayList() }
        }
    }

    override suspend fun deletePlayListFromFavorite(idPlayList: String) {
        dataBase.deletePlayList(idPlayList)
    }

}


