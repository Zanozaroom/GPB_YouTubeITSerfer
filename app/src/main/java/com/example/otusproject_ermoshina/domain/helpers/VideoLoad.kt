package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideo
import kotlinx.coroutines.flow.Flow

interface VideoLoad {
    suspend fun getLoadOneVideo(idVideo: String): YTVideo
    suspend fun saveVideo(video: YTVideo)
    suspend fun deleteVideo(video: YTVideo)
    fun loadFavoriteVideo(): Flow<List<YTVideo>>
}