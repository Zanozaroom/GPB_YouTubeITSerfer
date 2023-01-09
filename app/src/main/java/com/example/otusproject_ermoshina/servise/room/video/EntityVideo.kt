package com.example.otusproject_ermoshina.servise.room.video

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.otusproject_ermoshina.servise.room.model.RoomVideo

@Entity(tableName = "video")
data class EntityVideo(
    @PrimaryKey(autoGenerate = false)
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
    fun toVideo() = RoomVideo(idVideo, image,title,publishedAt,description,channelTitle,channelId,viewCount,likeCount)
}