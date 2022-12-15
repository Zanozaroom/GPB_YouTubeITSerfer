package com.example.otusproject_ermoshina.sources.repositories

import com.example.otusproject_ermoshina.base.Channel
import com.example.otusproject_ermoshina.sources.room.DaoChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class RepositoryYouTubeRoom(private val channelsDao: DaoChannels) {

    fun loadListChannels(): Flow<List<Channel>>{
       val listChannel = channelsDao.loadChannels().map {
            it.map{entityChannel ->
                entityChannel.toChannel()
            }
        }.flowOn(Dispatchers.IO)
       return listChannel
    }





}