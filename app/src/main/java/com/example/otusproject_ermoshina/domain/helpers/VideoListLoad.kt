package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult

interface VideoListLoad {
    suspend fun getVideoList(
        playListId: String,
        token: String,
        maxResult: Int
    ): ViewModelResult<YTVideoListPaging>

    suspend fun getLoadMoreVideoList(
        playList: YTVideoListPaging,
        maxResult: Int
    ): ViewModelResult<YTVideoListPaging>

    suspend fun loadVideoFromYouTubeForSave(idVideo: String): YTVideo?
    suspend fun saveVideo(video: YTVideo)


}
