package com.example.otusproject_ermoshina.servise.retrofit.model

import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.google.gson.annotations.SerializedName

data class ModelLoadListVideosResponse(
    @SerializedName("kind")
    var kind: String? = null,
    @SerializedName("etag")
    var etag: String? = null,
    @SerializedName("nextPageToken")
    var nextPageToken: String? = null,
    @SerializedName("items")
    var contentConteiner: List<ContentContainerDto>? = null,
    @SerializedName("pageInfo")
    var pageInfo: PageInfo? = null
)
{
    data class ContentContainerDto (
        @SerializedName("snippet")
        var detailsVideoList: DetailsVideoList? = null,
        @SerializedName("contentDetails")
        var contentDetails: ContentDetails? = null
    )
    data class ContentDetails (
        @SerializedName("videoId")
        var videoId: String? = null,
        @SerializedName("videoPublishedAt")
        var videoPublishedAt: String? = null
    )
    data class PageInfo (
        @SerializedName("totalResults")
        var totalResults:Int? = 0,
        @SerializedName("resultsPerPage")
        var resultsPerPage:Int? = 0
    )
    data class DetailsVideoList (
        @SerializedName("channelId")
        var channelId: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("thumbnails")
        var imageVideoList: ImageVideoList? = null,
        @SerializedName("channelTitle")
        var channelTitle: String? = null,
        @SerializedName("playlistId")
        var playlistId: String? = null,
        @SerializedName("videoOwnerChannelTitle")
        var videoOwnerChannelTitle: String? = null,
        @SerializedName("videoOwnerChannelId")
        var videoOwnerChannelId: String? = null
    )
    data class ImageVideoList (
        @SerializedName("standard")
        var standard: Standard? = null
    )
    data class Standard (
        @SerializedName("url")
        var url: String? = null
    )

    fun toVideoList() = contentConteiner?.map {
        YTVideoList(
            id = contentConteiner.hashCode(),
            idVideo = it.contentDetails?.videoId ?: NULL_DATA,
            title = it.detailsVideoList?.title ?: NULL_DATA,
            description = it.detailsVideoList?.description ?: NULL_DATA,
            image = it.detailsVideoList?.imageVideoList?.standard?.url ?: NULL_DATA,
            channelTitle = it.detailsVideoList?.channelTitle ?: NULL_DATA,
            videoPublishedAt = it.contentDetails?.videoPublishedAt ?: NULL_DATA,
            channelId = it.detailsVideoList?.channelId ?: NULL_DATA
        )
    }

companion object{
    private const val NULL_DATA = ""
}
}


