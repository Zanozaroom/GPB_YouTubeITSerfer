package com.example.otusproject_ermoshina.sources.room.video

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoVideo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorite(videoId: EntityVideo)

    @Delete
    fun deleteVideo(videoId: EntityVideo)

    @Query("SELECT * FROM video")
    fun loadVideo(): Flow<List<EntityVideo>>
}