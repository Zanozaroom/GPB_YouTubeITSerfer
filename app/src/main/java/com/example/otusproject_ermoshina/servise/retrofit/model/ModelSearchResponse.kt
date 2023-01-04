package com.example.otusproject_ermoshina.servise.retrofit.model


import com.example.otusproject_ermoshina.domain.model.YTSearch
import com.google.gson.annotations.SerializedName

data class ModelSearchResponse(
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null,
    @SerializedName("items")
    val resultSearchAllData: List<ResultSearchAllDataDto>? = null,
) {
    data class IdVideoDto (
        @SerializedName("kind")
        val kind: String? = null,
        @SerializedName("videoId")
        val videoId: String? = null,
    )
    data class ResultSearchAllDataDto(
        @SerializedName("kind")
        val kind: String? = null,
        @SerializedName("id")
        val idVideo: IdVideoDto? = null,
        @SerializedName("snippet")
        val searchContent: SearchContentDto? = null,
    )
    data class SearchContentDto(
        @SerializedName("publishedAt")
        val publishedAt: String? = null,
        @SerializedName("channelId")
        val channelId: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("thumbnails")
        val imageVideoSearchDto: ImageVideoSearchDto? = null,
        @SerializedName("channelTitle")
        val channelTitle: String? = null,
        @SerializedName("liveBroadcastContent")
        val liveBroadcastContent: String? = null,
        @SerializedName("publishTime")
        val publishTime: String? = null,
    )
    data class ImageVideoSearchDto(
        @SerializedName("medium")
        val medium: Medium? = null,
    )

    data class Medium(
        @SerializedName("url")
        val url: String? = null
    )

    fun toYTSearch(): List<YTSearch> =
        resultSearchAllData!!.map {
            YTSearch(
                videoId = it.idVideo?.videoId ?: NULL_DATA,
                channelId = it.searchContent?.channelId ?: NULL_DATA,
                title =  it.searchContent?.title ?: NULL_DATA,
                channelTitle = it.searchContent?.channelTitle ?: NULL_DATA,
                description = it.searchContent?.description ?: NULL_DATA,
                publishedAt = it.searchContent?.publishedAt ?: NULL_DATA,
                image = it.searchContent?.imageVideoSearchDto?.medium?.url ?: NULL_DATA
            )
        }

    companion object{
        private const val NULL_DATA = ""
    }
}
