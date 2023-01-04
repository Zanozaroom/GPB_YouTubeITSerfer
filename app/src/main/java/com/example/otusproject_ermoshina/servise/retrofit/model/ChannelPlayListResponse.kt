package com.example.otusproject_ermoshina.servise.retrofit.model


import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.google.gson.annotations.SerializedName

data class ChannelPlayListResponse(
    @SerializedName("nextPageToken")
    var nextPageToken: String? = null,
    @SerializedName("items")
    var listPlayList: List<ListPlayListDto>? = null
) {
    data class ListPlayListDto(
        @SerializedName("id")
        var idPlayList: String? = null,
        @SerializedName("snippet")
        var dataChannelAndPlayList: ChannelPlayListDto? = null
    )
    data class ChannelPlayListDto(
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
        var imagePlayList: ImagePlayListDto? = null

    )
    data class ImagePlayListDto(
        @SerializedName("standard")
        var standard: StandardImageSizeDto? = null
    )
    data class StandardImageSizeDto(
        @SerializedName("url")
        var url: String? = null
    )

    fun toChannelAndListVideos() = YTPlayListPaging(
        idChannel = listPlayList?.firstOrNull()?.dataChannelAndPlayList?.channelId ?: NULL_DATA,
        titleChannel = listPlayList?.firstOrNull()?.dataChannelAndPlayList?.channelTitle ?: NULL_DATA,
        nextToken = nextPageToken ?: NULL_DATA,
        listPlayList =  listPlayList!!.map {
            YTPlayList(
                idChannel = listPlayList?.firstOrNull()?.dataChannelAndPlayList?.channelId ?: NULL_DATA,
                idList = it.idPlayList ?: NULL_DATA,
                imageList = it.dataChannelAndPlayList?.imagePlayList?.standard?.url ?: NULL_DATA,
                titleListVideo = it.dataChannelAndPlayList?.title ?: NULL_DATA,
                titleChannel = listPlayList?.firstOrNull()?.dataChannelAndPlayList?.channelTitle ?: NULL_DATA
            )
        }
    )

    companion object {
        private const val NULL_DATA = ""
    }
}




