package com.example.otusproject_ermoshina.creatordata

import com.example.otusproject_ermoshina.domain.model.YTSearch
import com.example.otusproject_ermoshina.domain.model.YTSearchPaging

object CreatorYTSearchPaging {

    val listYTSearch = listOf(
        createYTSearch(videoId = "videoId1", channelId = "channelId1"),
        createYTSearch(videoId = "videoId2", channelId = "channelId2"),
        createYTSearch(videoId = "videoId3", channelId = "channelId3"),
        createYTSearch(videoId = "videoId4", channelId = "channelId4"),
        createYTSearch(videoId = "videoId5", channelId = "channelId5"),
        createYTSearch(videoId = "videoId6", channelId = "channelId6")
    )

    fun createYTSearchPaging(
        query: String = "query",
        nextToken: String = "nextToken",
        listSearchVideo: List<YTSearch> = listYTSearch
    ) = YTSearchPaging(
        query = query,
        nextToken = nextToken,
        listSearchVideo = listSearchVideo
    )

    fun createYTSearch(
        videoId: String = "videoId",
        channelId: String = "channelId",
        title: String = "title",
        channelTitle: String = "channelTitle",
        description: String = "description",
        publishedAt: String = "2011-08-12T20:17:46.384Z",
        image: String = ""
    ) = YTSearch(
        videoId = videoId,
        channelId = channelId,
        title = title,
        channelTitle = channelTitle,
        description = description,
        publishedAt = publishedAt,
        image = image
    )
}