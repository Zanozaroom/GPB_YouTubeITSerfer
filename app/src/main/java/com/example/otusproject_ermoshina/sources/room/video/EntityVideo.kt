package com.example.otusproject_ermoshina.sources.room.video

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.otusproject_ermoshina.sources.room.model.RoomVideo

@Entity(tableName = "video", indices = [Index(value = ["id_video"], unique = true)])
data class EntityVideo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_video") val idVideo: String,
    val image: String,
    val title: String,
    @ColumnInfo(name = "published_at") val publishedAt: String,
    val description: String,
    @ColumnInfo(name = "channel_title") val channelTitle: String,
    @ColumnInfo(name = "channel_id") val channelId: String,
    @ColumnInfo(name = "view_count") val viewCount: Int,
    @ColumnInfo(name = "like_count") val likeCount: Int,
) {
    fun toVideo() = RoomVideo(id,idVideo, image,title,publishedAt,description,channelTitle,channelId,viewCount,likeCount)
}