package com.example.otusproject_ermoshina.servise.room

import com.example.otusproject_ermoshina.servise.room.video.EntityVideo

object CreatorEntityRoom {
    fun getEntityVideo(
        idVideo: String = "idVideo",
        image: String = "image",
        title: String = "title",
        publishedAt: String = "publishedAt",
        description: String = "description",
        channelTitle: String = "channelTitle",
        channelId: String = "channelId",
        viewCount: Int = 0,
        likeCount: Int = 0
    ) = EntityVideo(
        idVideo = idVideo,
        image = image,
        title = title,
        publishedAt = publishedAt,
        description = description,
        channelTitle = channelTitle,
        channelId = channelId,
        viewCount = viewCount,
        likeCount = likeCount
    )
}