package com.example.otusproject_ermoshina.creatordata

import com.example.otusproject_ermoshina.domain.model.YTVideo

object CreatorYTVideo {
    fun createYTVideo(
        idVideo: String = "idVideo",
        image: String = "",
        title: String = "title",
        publishedAt: String = "2011-08-12T20:17:46.384Z",
        description: String = "description",
        channelTitle: String = "channelTitle",
        channelId: String = "channelId",
        viewCount: Int = 100,
        likeCount: Int = 100
    ) = YTVideo(
        idVideo =idVideo,
        image =image,
        title = title,
        publishedAt = publishedAt,
        description = description,
        channelTitle = channelTitle,
        channelId = channelId,
        viewCount = viewCount,
        likeCount = likeCount
    )
}