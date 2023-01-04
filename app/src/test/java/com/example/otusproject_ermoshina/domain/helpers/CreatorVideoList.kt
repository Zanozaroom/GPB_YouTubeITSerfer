package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse.*

class CreatorVideoList {
    companion object {
        fun createModelLoadListVideos(
            kind: String = "kind",
            etag: String = "etag",
            nextPageToken: String = "nextPageToken",
            contentContainer: List<ContentContainerDto> = listOf(
                ContentContainerDto(createDetailsVideoList(), createContentDetails())),
        ) = ModelLoadListVideosResponse(
            kind,
            etag,
            nextPageToken,
            contentContainer)


        fun createDetailsVideoList(
            channelId: String = "channelId",
            title: String = "title",
            description: String = "description",
            imageVideoList: ImageVideoList = ImageVideoList(getImageVideoList()),
            channelTitle: String = "channelTitle",
            playlistId: String = "playlistId",
            videoOwnerChannelTitle: String = "videoOwnerChannelTitle",
            videoOwnerChannelId: String = "videoOwnerChannelId"
        ) = DetailsVideoList(
            channelId,
            title,
            description,
            imageVideoList,
            channelTitle,
            playlistId,
            videoOwnerChannelTitle,
            videoOwnerChannelId
        )

        fun createContentDetails() = ContentDetails("videoId", "videoPublishedAt")

        fun getImageVideoList() = Standard("imageVideoListUrl")

        fun createYTVideoList(videoListResponse: ModelLoadListVideosResponse): YTVideoListPaging {
                val idPlayList = videoListResponse.contentConteiner!!.first().detailsVideoList!!.playlistId!!
                  return  YTVideoListPaging(
                    idPlayList = idPlayList,
                    nextToken = videoListResponse.nextPageToken ?: "",
                    listVideoList = videoListResponse.toVideoList()!!)
            }

        fun createFirstYTVideoListPaging(
            idPlayList: String = "idPlayList",
            nextToken: String = "nextPageToken",
            listVideoList: List<YTVideoList> = listOf(createYTVideoList())
        ) = YTVideoListPaging(
            idPlayList, nextToken, listVideoList
        )

        fun createYTVideoList(
            id: Int = 1,
            idVideo: String = "idVideo",
            title: String = "title",
            description: String = "description",
            image: String = "image",
            channelTitle: String = "channelTitle",
            videoPublishedAt: String = "videoPublishedAt",
            channelId: String = "channelId"
        ) = YTVideoList(
            id, idVideo, title, description, image, channelTitle, videoPublishedAt, channelId
        )

        fun createYTVideoListPaging(
            oldData: YTVideoListPaging, newData: ModelLoadListVideosResponse
        ): YTVideoListPaging {
                val concatVideoList = mutableListOf<YTVideoList>()
                concatVideoList.addAll(oldData.listVideoList)
                concatVideoList.addAll(newData.toVideoList()!!)
                 return YTVideoListPaging(
                    idPlayList = oldData.idPlayList,
                    nextToken = newData.nextPageToken ?: "",
                    listVideoList = concatVideoList
                )
            }
        }
    }

