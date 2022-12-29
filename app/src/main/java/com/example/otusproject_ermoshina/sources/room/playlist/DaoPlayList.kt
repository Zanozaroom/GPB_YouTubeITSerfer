package com.example.otusproject_ermoshina.sources.room.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoPlayList {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavoriteWithoutChannel(playListId: EntityPlayList)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addManyToFavorite(playListId: List<EntityPlayList>)

    @Query("SELECT * FROM playlist")
    fun loadPlayList(): Flow<List<EntityPlayList>>

    @Delete
    suspend fun deletePlayList(playListId: EntityPlayList)

}