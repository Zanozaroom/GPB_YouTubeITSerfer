package com.example.otusproject_ermoshina.servise.room.video

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoVideo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorite(videoId: EntityVideo)

    @Query("DELETE FROM video WHERE id_video = :videoId")
    fun deleteVideo(videoId: String)

    @Query("SELECT * FROM video")
    fun loadVideo(): Flow<List<EntityVideo>>
}