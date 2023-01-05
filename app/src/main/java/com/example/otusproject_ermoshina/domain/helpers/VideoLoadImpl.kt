package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.repositories.*
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoLoadImpl @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
) : VideoLoad {
    override suspend fun loadingYTVideo(idVideo: String): ViewModelResult<YTVideo> {
        val resultRepository = network.loadOneVideo(idVideo)
        return when(resultRepository){
            ErrorNetworkResult -> ErrorLoadingViewModel
            is SuccessNetworkResult -> {
                SuccessViewModel(resultRepository.dataNetworkResult.toVideo())
            }
            EmptyNetworkResult -> ErrorLoadingViewModel
        }
    }

    override suspend fun loadingYTVideoForSaving(idVideo: String): YTVideo? {
        val resultRepository = network.loadOneVideo(idVideo)
        return when(resultRepository){
            ErrorNetworkResult -> null
            is SuccessNetworkResult -> {
                when{
                    resultRepository.dataNetworkResult.resultContentHeads.isNullOrEmpty() -> null
                    else -> resultRepository.dataNetworkResult.toVideo()
                }
            }
            EmptyNetworkResult -> null
        }
    }

    override suspend fun saveVideo(video: YTVideo) {
        dataBase.addVideoToFavorite(video)
    }

    override suspend fun deleteVideo(video: YTVideo) {
        dataBase.deleteVideo(video)
    }

    override fun loadFavoriteVideo(): Flow<List<YTVideo>> {
       return dataBase.loadVideo()
    }
}