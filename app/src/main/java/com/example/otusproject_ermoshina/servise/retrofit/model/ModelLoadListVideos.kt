package com.example.otusproject_ermoshina.servise.retrofit.model

import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.google.gson.annotations.SerializedName

data class ModelLoadListVideos(
    @SerializedName("kind")
    var kind: String? = null,
    @SerializedName("etag")
    var etag: String? = null,
    @SerializedName("nextPageToken")
    var nextPageToken: String? = null,
    @SerializedName("items")
    var items: List<Item>? = null,
    @SerializedName("pageInfo")
    var pageInfo: PageInfo? = null
)
{
    data class Item (
        @SerializedName("kind")
        var kind: String? = null,
        @SerializedName("snippet")
        var snippet: Snippet? = null,
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
    data class Snippet (
        @SerializedName("channelId")
        var channelId: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("thumbnails")
        var thumbnails: Thumbnails? = null,
        @SerializedName("channelTitle")
        var channelTitle: String? = null,
        @SerializedName("playlistId")
        var playlistId: String? = null,
        @SerializedName("videoOwnerChannelTitle")
        var videoOwnerChannelTitle: String? = null,
        @SerializedName("videoOwnerChannelId")
        var videoOwnerChannelId: String? = null
    )
    data class Thumbnails (
        @SerializedName("standard")
        var standard: Standard? = null
    )
    data class Standard (
        @SerializedName("url")
        var url: String? = null
    )

    fun toVideoList() = items?.map {
        YTVideoList(
            id = items.hashCode(),
            idVideo = it.contentDetails?.videoId ?: "",
            title = it.snippet?.title ?: "",
            description = it.snippet?.description ?: "",
            image = it.snippet?.thumbnails?.standard?.url ?: "",
            channelTitle = it.snippet?.channelTitle ?: "",
            videoPublishedAt = it.contentDetails?.videoPublishedAt ?: "",
            channelId = it.snippet?.channelId ?: ""
        )
    }

}


