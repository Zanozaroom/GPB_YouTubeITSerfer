package com.example.otusproject_ermoshina.sources.repositories

import com.example.otusproject_ermoshina.base.Channel
import com.example.otusproject_ermoshina.sources.room.DaoChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryYouTubeRoom @Inject constructor(private val channelsDao: DaoChannels) {

   suspend fun loadListChannels(): List<Channel> =
            channelsDao.loadChannels().map { entityChannel ->
                    entityChannel.toChannel()
            }

}




