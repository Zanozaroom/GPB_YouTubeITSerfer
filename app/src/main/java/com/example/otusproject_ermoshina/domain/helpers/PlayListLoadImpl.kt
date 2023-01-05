package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.repositories.*
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PlayListLoadImpl @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase,
    @Named("Dispatchers.Default") val coroutineDispatcher: CoroutineDispatcher
) : PlayListLoad {

    override suspend fun firstLoadPlayList(
        idChannel: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTPlayListPaging> {
        val result = network.loadChannelList(idChannel, token, maxResult)
        return when (result) {
            is ErrorNetworkResult -> ErrorLoadingViewModel
            is SuccessNetworkResult -> filterYTPlayListPaging(result.dataNetworkResult.toChannelAndListVideos())
                ?: EmptyResultViewModel
            is EmptyNetworkResult -> EmptyResultViewModel
        }
    }

    override suspend fun loadMorePlayList(
        channel: YTPlayListPaging,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTPlayListPaging> {
        val result = network.loadChannelList(channel.idChannel, token, maxResult)
        return when (result) {
            is ErrorNetworkResult -> ErrorLoadingViewModel
            is EmptyNetworkResult -> NotMoreLoadingViewModel
            is SuccessNetworkResult -> {
                val matchedResult = result.dataNetworkResult.toChannelAndListVideos()
                val finishResult = filterYTPlayListPaging(matchedResult)

                if(finishResult == null) NotMoreLoadingViewModel
                else SuccessViewModel(createLoadMorePlayListAndChannel(
                    channel,finishResult.dataViewModelResult))
            }
        }
    }

    private suspend fun createLoadMorePlayListAndChannel(
        oldData: YTPlayListPaging,
        newData: YTPlayListPaging
    ): YTPlayListPaging = withContext(coroutineDispatcher) {
        val concatPlayList = mutableListOf<YTPlayList>()
        concatPlayList.addAll(oldData.listPlayList)
        concatPlayList.addAll(newData.listPlayList)

        YTPlayListPaging(
            newData.idChannel,
            newData.titleChannel,
            newData.nextToken,
            concatPlayList
        )
    }


    override suspend fun addToFavoritePlayList(playlist: YTPlayList) {
        dataBase.addPlayListToFavorite(playlist)
    }

    override fun loadFavoritePlayList(): Flow<List<YTPlayList>> {
        return dataBase.loadPlayList().map { listRoomPlayList ->
            listRoomPlayList.map { it.toYTPlayList() }
        }
    }

    override suspend fun deletePlayListFromFavorite(idPlayList: String) {
        dataBase.deletePlayList(idPlayList)
    }

    private fun filterYTPlayListPaging(responseData: YTPlayListPaging): SuccessViewModel<YTPlayListPaging>? {
        val notEmptyImage = responseData.listPlayList.filter {
            it.imageList.isNotBlank()
        }
        if (notEmptyImage.isEmpty()) return null
        val resultNotEmpty = YTPlayListPaging(
            idChannel = responseData.idChannel,
            titleChannel = responseData.titleChannel,
            nextToken = responseData.nextToken,
            listPlayList = notEmptyImage
        )
        return SuccessViewModel(resultNotEmpty)
    }
}



