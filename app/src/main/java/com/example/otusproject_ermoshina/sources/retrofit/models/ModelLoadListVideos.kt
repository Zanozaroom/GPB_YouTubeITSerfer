package com.example.otusproject_ermoshina.sources.retrofit.models

import android.graphics.pdf.PdfDocument
import com.example.otusproject_ermoshina.base.VideoList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelLoadListVideos (@SerializedName("kind")
val kind: String? = null,
                                @SerializedName("etag")
val etag: String? = null,
                                @SerializedName("nextPageToken")
val nextPageToken: String? = null,
                                @SerializedName("items")
val items: List<Item>? = null,
                                @SerializedName("pageInfo")
val pageInfo: PdfDocument.PageInfo? = null
) {
    data class Item(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("snippet")
        val snippet: Snippet? = null
    )

    data class Snippet(
        @SerializedName("publishedAt")
        val publishedAt: String? = null,
        @SerializedName("channelId")
        val channelId: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("thumbnails")
        val thumbnails: Thumbnails? = null,
        @SerializedName("resourceId")
        val resourceId: ResourceId? = null,
    )

    data class Thumbnails(
        @SerializedName("standard")
        val standard: Standard? = null
    )

    data class Standard(
        @SerializedName("url")
        val url: String? = null,
        @SerializedName("width")
        val width: Int?,
        @SerializedName("height")
        @Expose
        val height: Int?,
    )

    data class ResourceId(
        @SerializedName("kind")
        val kind: String? = null,
        @SerializedName("videoId")
        val videoId: String? = null
    )

    fun toVideoList() = items?.map {
        VideoList(
        idVideo = it.snippet?.resourceId?.videoId,
        title = it.snippet?.title,
        description = it.snippet?.description,
        image = it.snippet?.thumbnails?.standard?.url
    )
    }
}