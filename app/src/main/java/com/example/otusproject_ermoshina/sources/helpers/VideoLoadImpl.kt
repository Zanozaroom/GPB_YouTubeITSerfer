package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.RepositoryNetwork
import javax.inject.Inject

class VideoLoadImpl @Inject constructor(
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
):VideoLoad {
    override suspend fun getLoadOneVideo(idVideo: String): YTVideo {
        return network.loadOneVideo(idVideo).toVideo()
    }
}