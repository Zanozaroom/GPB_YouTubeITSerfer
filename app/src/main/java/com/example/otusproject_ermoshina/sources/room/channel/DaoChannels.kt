package com.example.otusproject_ermoshina.sources.room.channel

import androidx.room.*
import com.example.otusproject_ermoshina.sources.room.channel.EntityChannels
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayList

/***
 * Этот интерфейс будет описывать реализации, которые нужны при работе с базой донных
 * Это загрузка , выборка и тд
 * Всю реализацию данных операций сделает сама библиотека room
 */
@Dao
interface DaoChannels {
    @Query("SELECT * FROM channels")
    suspend fun loadChannels(): List<ChannelsAndPlayList>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(channel: EntityChannels)

    @Delete
    suspend fun deleteChannel(channel: EntityChannels)
}