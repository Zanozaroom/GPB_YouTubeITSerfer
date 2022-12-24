package com.example.otusproject_ermoshina.sources

import com.example.otusproject_ermoshina.base.YTChannelAndListVideos
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.sources.room.channel.ChannelsAndPlayList
import com.example.otusproject_ermoshina.sources.room.model.RoomPlayList
import com.example.otusproject_ermoshina.sources.room.model.RoomQueryApp

interface RepositoryDataBase {

    suspend fun addPlayListToFavorite(playList: YTPlayListOfChannel)
    suspend fun loadPlayList(): List<RoomPlayList>
    suspend fun deletePlayList(playList:YTPlayListOfChannel)

    suspend fun loadQuery():List<RoomQueryApp>

    suspend fun addVideoToFavorite(video:YTVideo)
    suspend fun deleteVideo(video: YTVideo)
    suspend fun loadVideo(): List<YTVideo>
}