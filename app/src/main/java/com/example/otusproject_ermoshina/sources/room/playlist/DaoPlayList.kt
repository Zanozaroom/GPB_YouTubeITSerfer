package com.example.otusproject_ermoshina.sources.room.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DaoPlayList {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(playListId: EntityPlayList)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavoriteWithoutChannel(playListId: EntityPlayListWithoutChannel)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addManyToFavorite(playListId: List<EntityPlayList>)

    @Query("SELECT * FROM play_lists")
    suspend fun loadPlayList(): List<EntityPlayList>

    @Delete
    suspend fun deletePlayList(playListId: EntityPlayListWithoutChannel)

}