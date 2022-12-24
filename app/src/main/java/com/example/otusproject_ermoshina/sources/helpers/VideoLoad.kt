package com.example.otusproject_ermoshina.sources.helpers

import com.example.otusproject_ermoshina.base.YTVideo

interface VideoLoad {
    suspend fun getLoadOneVideo(idVideo: String): YTVideo
}