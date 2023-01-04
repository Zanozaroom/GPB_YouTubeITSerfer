package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.servise.room.playlist.EntityPlayList
import com.example.otusproject_ermoshina.servise.room.query.EntityStartQuery
import com.example.otusproject_ermoshina.servise.room.video.EntityVideo

object CreatorRepositoryRoom {
    fun toEntityPlayList(playList: YTPlayList) = EntityPlayList(
        idChannel = playList.idChannel,
        idPlayList = playList.idList,
        image = playList.imageList,
        titlePlayList = playList.titleListVideo,
        titleChannel = playList.titleChannel
    )
    fun toEntityVideo(video: YTVideo) = EntityVideo(
        idVideo = video.idVideo,
        image =  video.image,
        title = video.title,
        publishedAt = video.publishedAt,
        description = video.description,
        channelTitle = video.channelTitle,
        channelId = video.channelId,
        viewCount = video.viewCount,
        likeCount = video.likeCount
    )
    fun createYTVideo(
        idVideo: String = "idVideo",
        image: String = "image",
        title: String = "title",
        publishedAt: String = "publishedAt",
        description: String = "description",
        channelTitle: String = "channelTitle",
        channelId: String = "channelId",
        viewCount: Int = 0,
        likeCount: Int = 0
    ) = YTVideo(
        idVideo, image, title, publishedAt, description, channelTitle, channelId, viewCount, likeCount
    )

    fun createEntityStartQuery (
        id: Int = 1,
        query: String = "query",
        titleQuery: String = "titleQuery"
    ) = EntityStartQuery(id, query, titleQuery)

}