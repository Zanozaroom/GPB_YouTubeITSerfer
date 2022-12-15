package com.example.otusproject_ermoshina.sources.room

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/***
 * Этот интерфейс будет описывать реализации, которые нужны при работе с базой донных
 * Это загрузка , выборка и тд
 * Всю реализацию данных операций сделает сама библиотека room
 */
@Dao
interface DaoChannels {
    @Query("SELECT * FROM channels")
    fun loadChannels(): Flow<List<EntityChannels>>
}