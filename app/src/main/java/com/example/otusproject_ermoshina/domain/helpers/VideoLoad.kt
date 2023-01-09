package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import kotlinx.coroutines.flow.Flow

interface VideoLoad {
    suspend fun loadingYTVideo(idVideo: String): ViewModelResult<YTVideo>
    suspend fun loadingYTVideoForSaving(idVideo: String): YTVideo?
    suspend fun saveVideo(video: YTVideo)
    suspend fun deleteVideo(video: YTVideo)
    fun loadFavoriteVideo(): Flow<List<YTVideo>>
}