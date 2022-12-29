package com.example.otusproject_ermoshina.sources.retrofit.models

import com.example.otusproject_ermoshina.base.YTVideo
import com.google.gson.annotations.SerializedName

data class ModelLoadVideo(
    @SerializedName("kind")
    var kind: String? = null,
    @SerializedName("etag")
    var etag: String? = null,
    @SerializedName("items")
    var items: List<Item>? = null
) {
    data class Item (
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("snippet")
    var snippet: Snippet? = null,
    @SerializedName("statistics")
    var statistics: Statistics? = null
    )
    data class Snippet (
        @SerializedName("publishedAt")
        var publishedAt: String? = null,
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
        @SerializedName("tags")
        var tags: List<String>? = null,
        @SerializedName("categoryId")
        var categoryId: String? = null
    )
    data class Thumbnails (
        @SerializedName("standard")
        var standard: Standard? = null
    )
    data class Standard (
        @SerializedName("url")
        var url: String? = null
    )
    data class Statistics (
        @SerializedName("viewCount")
        var viewCount: String? = null,
        @SerializedName("likeCount")
        var likeCount: String? = null,
        @SerializedName("favoriteCount")
        var favoriteCount: String? = null
    )

   fun toVideo():YTVideo =
       items?.first().let {
           YTVideo(
               idVideo = it?.id ?:NULL_DATA,
               image = it?.snippet?.thumbnails?.standard?.url ?: NULL_DATA,
               title = it?.snippet?.title ?: NULL_DATA,
               publishedAt = it?.snippet?.publishedAt ?: NULL_DATA,
               description = it?.snippet?.description ?: NULL_DATA,
               channelTitle = it?.snippet?.channelTitle ?: NULL_DATA,
               channelId =it?.snippet?.channelId ?: NULL_DATA,
               viewCount = it?.statistics?.viewCount?.toInt() ?: NULL_INT,
               likeCount = it?.statistics?.likeCount?.toInt() ?: NULL_INT
           )
       }

companion object {
    private const val NULL_DATA = ""
    private const val NULL_INT = 0
}
}
