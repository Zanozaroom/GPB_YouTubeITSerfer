package com.example.otusproject_ermoshina.sources.retrofit.models


import com.example.otusproject_ermoshina.base.ChannelAndListVideos
import com.example.otusproject_ermoshina.base.PlayListOfChannel
import com.google.gson.annotations.SerializedName


data class ModelChannelPlayList(
    @SerializedName("nextPageToken")
    var nextPageToken: String? = null,
    @SerializedName("items")
    var items: List<Item>? = null
) {
    data class Item(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("snippet")
        var snippet: Snippet? = null
    )

    data class Snippet(
        @SerializedName("publishedAt")
        var publishedAt: String? = null,
        @SerializedName("channelId")
        var channelId: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("channelTitle")
        var channelTitle: String? = null,
        @SerializedName("thumbnails")
        var thumbnails: Thumbnails? = null

    )

    data class PageInfo(
        @SerializedName("totalResults")
        var totalResults: Int = 0,
        @SerializedName("resultsPerPage")
        var resultsPerPage: Int = 0
    )

    data class Thumbnails(
        @SerializedName("standard")
        var standard: Standard? = null
    )

    data class Standard(
        @SerializedName("url")
        var url: String? = null
    )

    fun toChannelAndListVideos(): ChannelAndListVideos {
        var titleChannel = ""
        val idChannel = items?.firstOrNull()?.snippet?.channelId ?:NULL_DATA
        val nextToken = nextPageToken ?: NULL_DATA
        titleChannel = items?.firstOrNull()?.snippet?.channelTitle ?: TITLE_CHANNEL
        val videoChannelAndListVideos =
                 items!!.map {
                    PlayListOfChannel(
                        idList = it.id ?: NULL_DATA,
                        imageList = it.snippet?.thumbnails?.standard?.url ?: IMAGE,
                        titleListVideo = it.snippet?.title ?: TITLE_LIST_VIDEO,
                        nextToken = nextToken
                    )
            }
        return ChannelAndListVideos(idChannel, titleChannel,nextToken,videoChannelAndListVideos)
    }
    companion object {
        private const val NULL_DATA = ""
        private const val IMAGE = ""
        private const val TITLE_CHANNEL = "Not title, sorry"
        private const val TITLE_LIST_VIDEO = "Not title, sorry"
    }
}




