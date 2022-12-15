package com.example.otusproject_ermoshina.sources.retrofit.models

import com.example.otusproject_ermoshina.base.Video
import com.google.gson.annotations.SerializedName

data class ModelLoadVideo(
    @SerializedName("items")
    var items: List<Item>? = null
) {
    data class Item(
        @SerializedName("kind")
        var kind: String? = null,
        @SerializedName("etag")
        var etag: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("snippet")
        var snippet: Snippet? = null,
        @SerializedName("contentDetails")
        var contentDetails: ContentDetails? = null
    )

    data class Snippet(
        @SerializedName("publishedAt")
        var publishedAt: String? = null,
        @SerializedName("channelId")
        var channelId: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("thumbnails")
        var thumbnails: Thumbnails? = null,
        @SerializedName("channelTitle")
        var channelTitle: String? = null
    )

    data class Thumbnails(
        @SerializedName("medium")
        var medium: Medium? = null
    )

    data class ContentDetails(
        @SerializedName("duration")
        var duration: String? = null,
        @SerializedName("dimension")
        var dimension: String? = null,
        @SerializedName("definition")
        var definition: String? = null,
        @SerializedName("caption")
        var caption: String? = null,
        @SerializedName("licensedContent")
        var licensedContent: Boolean = false,
        @SerializedName("projection")
        var projection: String? = null
    )

    data class Medium(
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("width")
        var width: Int = 0,
        @SerializedName("height")
        var height: Int = 0,
    )

    fun toVideo() =
        items?.map {
            Video(
                id = it.id!!,
                title = it.snippet!!.title!!,
                description = it.snippet!!.description
            )
        }

}