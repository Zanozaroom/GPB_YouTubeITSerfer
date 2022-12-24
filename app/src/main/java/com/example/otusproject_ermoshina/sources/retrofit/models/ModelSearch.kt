package com.example.otusproject_ermoshina.sources.retrofit.models


import com.example.otusproject_ermoshina.base.YTSearch
import com.google.gson.annotations.SerializedName

data class ModelSearch(
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("etag")
    val etag: String? = null,
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null,
    @SerializedName("regionCode")
    val regionCode: String? = null,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo? = null,
    @SerializedName("items")
    val items: List<Item>? = null,
) {
    data class Id (
        @SerializedName("kind")
        val kind: String? = null,
        @SerializedName("videoId")
        val videoId: String? = null,
    )
    data class Item(
        @SerializedName("kind")
        val kind: String? = null,
        @SerializedName("etag")
        val etag: String? = null,
        @SerializedName("id")
        val id: Id? = null,
        @SerializedName("snippet")
        val snippet: Snippet? = null,
    )
    data class PageInfo(
        @SerializedName("totalResults")
        val totalResults: Int? = 0,
        @SerializedName("resultsPerPage")
        val resultsPerPage: Int? = 0,
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
        @SerializedName("channelTitle")
        val channelTitle: String? = null,
        @SerializedName("liveBroadcastContent")
        val liveBroadcastContent: String? = null,
        @SerializedName("publishTime")
        val publishTime: String? = null,
    )
    data class Thumbnails(
        @SerializedName("medium")
        val medium: Medium? = null,
    )

    data class Medium(
        @SerializedName("url")
        val url: String? = null
    )

    fun toYTSearch(): List<YTSearch> =
        items!!.map {
            YTSearch(
                id = etag.hashCode(),
                videoId = it.id?.videoId ?: NULL_DATA,
                channelId = it.snippet?.channelId ?: NULL_DATA,
                title =  it.snippet?.title ?: NULL_DATA,
                channelTitle = it.snippet?.channelTitle ?: NULL_DATA,
                description = it.snippet?.description ?: NULL_DATA,
                publishedAt = it.snippet?.publishedAt ?: NULL_DATA,
                image = it.snippet?.thumbnails?.medium?.url ?: NULL_DATA
            )
        }

    companion object{
        private const val NULL_DATA = ""
    }
}
