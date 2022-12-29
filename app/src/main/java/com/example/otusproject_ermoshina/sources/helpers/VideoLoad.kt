package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTVideo
import kotlinx.coroutines.flow.Flow

interface VideoLoad {
    suspend fun getLoadOneVideo(idVideo: String): YTVideo
    suspend fun saveVideo(video: YTVideo)
    suspend fun deleteVideo(video: YTVideo)
    fun loadFavoriteVideo(): Flow<List<YTVideo>>
}