package com.example.otusproject_ermoshina.domain.mapping

import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.servise.room.model.RoomPlayList
import com.example.otusproject_ermoshina.servise.room.model.RoomQueryApp
import com.example.otusproject_ermoshina.servise.room.model.RoomVideo
import com.example.otusproject_ermoshina.servise.room.playlist.EntityPlayList
import com.example.otusproject_ermoshina.servise.room.query.EntityStartQuery
import com.example.otusproject_ermoshina.servise.room.video.EntityVideo
import org.junit.Assert
import org.junit.Test

class RoomMappingTest {

    @Test
    fun `mappingToYTPlayList GetDataFromRoomPlayList MapToYTPlayList`() {
        val roomPlayList = RoomPlayList(
            idChannel = "idChannel",
            idPlayList = "idPlayList",
            imageList = "imageList",
            titleListVideo = "titleListVideo",
            titleChannel = "titleChannel"
        )
        val yTPlayList = YTPlayList(
            idChannel = "idChannel",
            idList = "idPlayList",
            imageList = "imageList",
            titleListVideo = "titleListVideo",
            titleChannel = "titleChannel"
        )

        val result = roomPlayList.toYTPlayList()

        Assert.assertEquals(yTPlayList, result)
    }

    @Test
    fun `mappingToYTVideo GetDataFromRoomPlayList MapToYTPlayList`() {
        val roomVideo = RoomVideo(
            idVideo = "idVideo",
            image = "image",
            title = "title",
            publishedAt = "publishedAt",
            description = "description",
            channelTitle = "channelTitle",
            channelId = "channelId",
            viewCount = 0,
            likeCount = 0
        )
        val ytVideo = YTVideo(
            idVideo = "idVideo",
            image = "image",
            title = "title",
            publishedAt = "publishedAt",
            description = "description",
            channelTitle = "channelTitle",
            channelId = "channelId",
            viewCount = 0,
            likeCount = 0
        )

        val result = roomVideo.toYTVideo()

        Assert.assertEquals(ytVideo, result)
    }

    @Test
    fun `mappingToRoomPlayList GetDataFromEntityPlayList MapToRoomPlayList`() {
        val entityPlayList = EntityPlayList(
            idPlayList = "idPlayList",
            idChannel = "idChannel",
            image = "image",
            titlePlayList = "titlePlayList",
            titleChannel = "titleChannel"
        )
        val roomPlayList = RoomPlayList(
            idChannel = "idChannel",
            idPlayList = "idPlayList",
            imageList = "image",
            titleListVideo = "titlePlayList",
            titleChannel = "titleChannel"
        )

        val result = entityPlayList.toPlayList()

        Assert.assertEquals(roomPlayList, result)
    }

    @Test
    fun `mappingToRoomQueryApp GetDataFromEntityStartQuery MapToRoomQueryApp`() {
        val entityStartQuery = EntityStartQuery(
            id = 0,
            query = "query",
            titleQuery = "titleQuery"
        )
        val roomQueryApp = RoomQueryApp(
            idQueryApp = 0,
            query = "query",
            titleQuery = "titleQuery"
        )

        val result = entityStartQuery.toRoomQueryApp()

        Assert.assertEquals(roomQueryApp, result)
    }

    @Test
    fun `mappingToRoomVideo GetDataFromEntityVideo MapToRoomVideo`() {
        val entityVideo = EntityVideo(
            idVideo = "idVideo",
            image = "image",
            title = "title",
            publishedAt = "publishedAt",
            description = "description",
            channelTitle = "channelTitle",
            channelId = "channelId",
            viewCount = 0,
            likeCount = 0)
        val roomVideo = RoomVideo(
            idVideo = "idVideo",
            image = "image",
            title = "title",
            publishedAt = "publishedAt",
            description = "description",
            channelTitle = "channelTitle",
            channelId = "channelId",
            viewCount = 0,
            likeCount = 0)

        val result = entityVideo.toVideo()

        Assert.assertEquals(roomVideo, result)
    }
}