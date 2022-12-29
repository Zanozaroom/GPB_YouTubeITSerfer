package com.example.otusproject_ermoshina.servise.room.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoPlayList {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(playListId: EntityPlayList)

    @Query("SELECT * FROM playlist")
    fun loadPlayList(): Flow<List<EntityPlayList>>

    @Query("DELETE FROM playlist WHERE id_playlist = :playListId")
    suspend fun deletePlayList(playListId: String)

}