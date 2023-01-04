package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.servise.retrofit.model.ChannelPlayListResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ChannelPlayListResponse.*

object CreatorYouTubeRepository {
    fun createChannelPlayListResponse(
        nextPageToken: String = "nextPageToken",
        listPlayList: List<ListPlayListDto>? = listOf(createListPlayListDto())
    ) = ChannelPlayListResponse(nextPageToken, listPlayList)

    fun createListPlayListDto(
        idPlayList: String = "idPlayList",
        dataChannelAndPlayList: ChannelPlayListDto = createChannelPlayListDto()
    ) = ListPlayListDto(idPlayList, dataChannelAndPlayList)

    fun createChannelPlayListDto(
        publishedAt: String = "publishedAt",
        channelId: String = "channelId",
        title: String = "title",
        description: String = "description",
        channelTitle: String = "channelTitle",
        imagePlayList: ImagePlayListDto = createImagePlayListDto()
    ) = ChannelPlayListDto(publishedAt, channelId, title, description, channelTitle, imagePlayList)

    fun createImagePlayListDto(
        standardImage: StandardImageSizeDto = StandardImageSizeDto("url")
    ) = ImagePlayListDto(standardImage)

}