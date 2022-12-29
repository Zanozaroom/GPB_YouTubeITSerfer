package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.servise.room.model.RoomPlayList
import com.example.otusproject_ermoshina.servise.room.model.RoomQueryApp
import kotlinx.coroutines.flow.Flow

interface RepositoryDataBase {

    suspend fun addPlayListToFavorite(playList: YTPlayList)
    fun loadPlayList(): Flow<List<RoomPlayList>>
    suspend fun deletePlayList(idPlayList: String)

    suspend fun loadQuery():List<RoomQueryApp>

    suspend fun addVideoToFavorite(video: YTVideo)
    suspend fun deleteVideo(video: YTVideo)
    fun loadVideo(): Flow<List<YTVideo>>
}