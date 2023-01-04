package com.example.otusproject_ermoshina.domain.mapping

import com.example.otusproject_ermoshina.domain.helpers.CreatorSearch
import com.example.otusproject_ermoshina.domain.helpers.CreatorVideo
import com.example.otusproject_ermoshina.domain.helpers.CreatorVideoList
import com.example.otusproject_ermoshina.domain.model.*
import com.example.otusproject_ermoshina.domain.repositories.CreatorYouTubeRepository
import org.junit.Assert
import org.junit.Test

class RetrofitMappingTest {

    @Test
    fun `mappingToChannelAndListVideos GetDataFromChannelPlayListResponse MapToChannelAndListVideos`() {
        val channelPlayListResponse = CreatorYouTubeRepository.createChannelPlayListResponse()
        val listPlayList = YTPlayList(
            idChannel = "channelId",
            idList = "idPlayList",
            imageList = "url",
            titleListVideo = "title",
            titleChannel = "channelTitle")

        val ytPlayListPaging = YTPlayListPaging(
            idChannel = "channelId",
            titleChannel = "channelTitle",
            nextToken = "nextPageToken",
            listPlayList = listOf(listPlayList))

       val result = channelPlayListResponse.toChannelAndListVideos()

        Assert.assertEquals(ytPlayListPaging, result)
    }

    @Test
    fun `mappingToVideoList GetDataFromModelLoadListVideosResponse MapToVideoList`(){
        val modelLoadVideoList = CreatorVideoList.createModelLoadListVideos()
        val videoList = listOf(YTVideoList(
            id = modelLoadVideoList.contentConteiner.hashCode(),
            idVideo = "videoId",
            title = "title",
            description = "description",
            image = "imageVideoListUrl",
            channelTitle = "channelTitle",
            videoPublishedAt = "videoPublishedAt",
            channelId = "channelId"
        ))

        val result = modelLoadVideoList.toVideoList()

        Assert.assertEquals(videoList, result)
    }

    @Test
    fun `mappingToYTVideo GetDataFromModelLoadVideoResponse MapToYTVideo`(){
        val modelLoadVideo = CreatorVideo.createModelLoadVideo()
        val video = YTVideo(
                idVideo = "id",
                image = "",
                title = "title",
                publishedAt = "publishedAt",
                description = "description",
                channelTitle = "channelTitle",
                channelId ="channelId",
                viewCount = 0,
                likeCount = 0)


        val result = modelLoadVideo.toVideo()

        Assert.assertEquals(video, result)
    }


    @Test
    fun `mappingToYTSearch GetDataFromModelSearchResponse MapToYTSearch`(){
        val modeYTSearch = CreatorSearch.createModelSearch()
        val ytSearch = listOf(YTSearch(
            videoId = "IdVideo",
            channelId = "",
            title = "",
            channelTitle = "",
            description = "",
            publishedAt = "",
            image =""),
            YTSearch(
                videoId = "IdVideo",
                channelId = "",
                title = "",
                channelTitle = "",
                description = "",
                publishedAt = "",
                image =""))

        val result = modeYTSearch.toYTSearch()

        Assert.assertEquals(ytSearch, result)
    }

}