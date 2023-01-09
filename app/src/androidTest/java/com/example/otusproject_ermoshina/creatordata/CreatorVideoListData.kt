package com.example.otusproject_ermoshina.creatordata

import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging

object CreatorVideoListData {
    fun createYTVideoListPaging(
        idPlayList: String = "idPlayList",
        nextToken: String = "nextToken",
        listVideoList: List<YTVideoList> = listYTVideoList
    ) = YTVideoListPaging(
        idPlayList = idPlayList,
        nextToken = nextToken,
        listVideoList = listVideoList
    )

    val listYTVideoList = listOf(
        createYTVideoList(id =1, idVideo = "idVideo1"),
        createYTVideoList(id =2, idVideo = "idVideo2"),
        createYTVideoList(id =3, idVideo = "idVideo3"),
        createYTVideoList(id =4, idVideo = "idVideo4"),
        createYTVideoList(id =5, idVideo = "idVideo5"),
    )
    fun createYTVideoList(
        id: Int = 1,
        idVideo: String = "idVideo",
        title: String = "titleVideo",
        description: String = "descriptionVideo",
        image: String = "",
        channelTitle: String = "channelTitle",
        videoPublishedAt: String = "2011-08-12T20:17:46.384Z",
        channelId: String = "channelId"
    ) = YTVideoList(
        id = id,
        idVideo = idVideo,
        title =title,
        description = description,
        image = image,
        channelTitle = channelTitle,
        videoPublishedAt = videoPublishedAt,
        channelId = channelId
    )
}