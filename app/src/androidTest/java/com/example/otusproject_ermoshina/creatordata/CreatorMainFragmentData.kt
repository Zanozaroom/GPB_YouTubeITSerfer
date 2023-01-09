package com.example.otusproject_ermoshina.creatordata

import com.example.otusproject_ermoshina.domain.model.YTMainFragmentData
import com.example.otusproject_ermoshina.domain.model.YTSearch

object CreatorMainFragmentData {
    fun createResultForRecyclerMainFragment() = listOf(
        createYTMainFragmentData(id = 1, query = "query1"),
        createYTMainFragmentData(id = 2, query = "query2"),
        createYTMainFragmentData(id = 3, query = "query3"),
        createYTMainFragmentData(id = 4, query = "query4"),
        createYTMainFragmentData(id = 5, query = "query5"),
        createYTMainFragmentData(id = 6, query = "query6")
    )

    fun createYTMainFragmentData(
        id: Int = 1,
        query: String = "query",
        title: String = "title",
        listResult: List<YTSearch> = listYTSearch
    ) = YTMainFragmentData(
        id = id,
        query = query,
        title = title,
        listResult = listResult
    )

    fun createYTSearch(
        videoId: String = "videoId",
        channelId: String = "channelId",
        title: String = "title",
        channelTitle: String = "channelTitle",
        description: String = "description",
        publishedAt: String = "publishedAt",
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

    val listYTSearch = listOf(
        createYTSearch(videoId = "videoId1"),
        createYTSearch(videoId = "videoId2"),
        createYTSearch(videoId = "videoId3"),
        createYTSearch(videoId = "videoId4"),
        createYTSearch(videoId = "videoId5"),
        createYTSearch(videoId = "videoId6")
    )

}