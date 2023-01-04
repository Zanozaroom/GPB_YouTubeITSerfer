package com.example.otusproject_ermoshina.servise.retrofit.model

import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.google.gson.annotations.SerializedName

data class ModelLoadVideoResponse(
    @SerializedName("kind")
    var kind: String? = null,
    @SerializedName("etag")
    var etag: String? = null,
    @SerializedName("items")
    var resultContentHeads: List<ResultContentHeadDto>? = null
) {
    data class ResultContentHeadDto (
        @SerializedName("id")
    var id: String? = null,
        @SerializedName("snippet")
    var contentInfoVideo: ContentInfoVideoDto? = null,
        @SerializedName("statistics")
    var statistics: StatisticsDto? = null
    )
    data class ContentInfoVideoDto (
        @SerializedName("publishedAt")
        var publishedAt: String? = null,
        @SerializedName("channelId")
        var channelId: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("thumbnails")
        var image: ImageDto? = null,
        @SerializedName("channelTitle")
        var channelTitle: String? = null,
        @SerializedName("tags")
        var tags: List<String>? = null,
        @SerializedName("categoryId")
        var categoryId: String? = null
    )
    data class ImageDto (
        @SerializedName("standard")
        var standard: StandardImageDto? = null
    )
    data class StandardImageDto (
        @SerializedName("url")
        var url: String? = null
    )
    data class StatisticsDto (
        @SerializedName("viewCount")
        var viewCount: String? = null,
        @SerializedName("likeCount")
        var likeCount: String? = null,
        @SerializedName("favoriteCount")
        var favoriteCount: String? = null
    )

   fun toVideo(): YTVideo =
       resultContentHeads?.first().let {
           YTVideo(
               idVideo = it?.id ?: NULL_DATA,
               image = it?.contentInfoVideo?.image?.standard?.url ?: NULL_DATA,
               title = it?.contentInfoVideo?.title ?: NULL_DATA,
               publishedAt = it?.contentInfoVideo?.publishedAt ?: NULL_DATA,
               description = it?.contentInfoVideo?.description ?: NULL_DATA,
               channelTitle = it?.contentInfoVideo?.channelTitle ?: NULL_DATA,
               channelId =it?.contentInfoVideo?.channelId ?: NULL_DATA,
               viewCount = it?.statistics?.viewCount?.toInt() ?: NULL_INT,
               likeCount = it?.statistics?.likeCount?.toInt() ?: NULL_INT
           )
       }

companion object {
    private const val NULL_DATA = ""
    private const val NULL_INT = 0
}
}
