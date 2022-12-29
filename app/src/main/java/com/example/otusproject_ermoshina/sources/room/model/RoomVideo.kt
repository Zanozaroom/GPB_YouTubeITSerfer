package com.example.otusproject_ermoshina.sources.room.model

import com.example.otusproject_ermoshina.base.YTVideo

data class RoomVideo(
    val id: Int,
    val idVideo: String,
    val image: String,
    val title: String,
    val publishedAt: String,
    val description: String,
    val channelTitle: String,
    val channelId: String,
    val viewCount: Int,
    val likeCount: Int
) {
    fun toYTVideo() =
    YTVideo(
    idVideo = idVideo,
        image = image,
        title = title,
        publishedAt = publishedAt,
        description = description,
        channelTitle = channelTitle,
        channelId= channelId,
        viewCount = viewCount,
        likeCount = likeCount
    )
}

