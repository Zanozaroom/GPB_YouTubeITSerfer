package com.example.otusproject_ermoshina.sources.repositories

import com.example.otusproject_ermoshina.base.Channel
import com.example.otusproject_ermoshina.sources.room.DaoChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RepositoryYouTubeRoom(private val channelsDao: DaoChannels) {

   suspend fun loadListChannels(): List<Channel> =
            channelsDao.loadChannels().map { entityChannel ->
                    entityChannel.toChannel()
            }

}




