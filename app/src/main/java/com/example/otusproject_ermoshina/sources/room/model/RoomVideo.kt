package com.example.otusproject_ermoshina.sources.room.model

import com.example.otusproject_ermoshina.base.YTVideo

data class RoomVideo(
    val id: Int,
    val idVideo: String,
    val title: String,
    val description: String,
    val channelTitle: String,
    val viewCount: Int,
    val likeCount: Int
) {
    fun toYTVideo() =
    YTVideo(
    idVideo = idVideo,
        title = title,
        description = description,
        channelTitle = channelTitle,
        viewCount = viewCount,
        likeCount = likeCount
    )
}

