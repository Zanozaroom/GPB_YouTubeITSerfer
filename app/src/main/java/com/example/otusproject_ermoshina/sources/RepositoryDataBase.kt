package com.example.otusproject_ermoshina.sources

import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.sources.room.model.RoomPlayList
import com.example.otusproject_ermoshina.sources.room.model.RoomQueryApp
import kotlinx.coroutines.flow.Flow

interface RepositoryDataBase {

    suspend fun addPlayListToFavorite(playList: YTPlayList)
    fun loadPlayList(): Flow<List<RoomPlayList>>
    suspend fun deletePlayList(playList:YTPlayList)

    suspend fun loadQuery():List<RoomQueryApp>

    suspend fun addVideoToFavorite(video:YTVideo)
    suspend fun deleteVideo(video: YTVideo)
    fun loadVideo(): Flow<List<YTVideo>>
}