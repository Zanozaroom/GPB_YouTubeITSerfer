package com.example.otusproject_ermoshina.sources.room.video

import androidx.room.*
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayList

@Dao
interface DaoVideo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorite(videoId: EntityVideo)

    @Delete
    fun deleteVideo(videoId: EntityVideo)

    @Query("SELECT * FROM video")
    suspend fun loadVideo(): List<EntityVideo>
}