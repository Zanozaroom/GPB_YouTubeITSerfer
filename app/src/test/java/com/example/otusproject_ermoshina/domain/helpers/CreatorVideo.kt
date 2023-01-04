package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideoResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideoResponse.*

class CreatorVideo {
    companion object{
        fun createModelLoadVideo(
        kind: String = "kind",
        etag: String = "etag",
        resultContentHeads: List<ResultContentHeadDto>? = listOf(createResultContentHead())
        ) = ModelLoadVideoResponse(kind, etag, resultContentHeads)

        fun createResultContentHead(
            id: String = "id",
            contentInfoVideo: ContentInfoVideoDto = createContentInfoVideo(),
            statistics: StatisticsDto? = null
        ) = ResultContentHeadDto(id, contentInfoVideo, statistics)

        fun createContentInfoVideo(
            publishedAt: String = "publishedAt",
            channelId: String = "channelId",
            title: String = "title",
            description: String = "description",
            image: ImageDto? = null,
            channelTitle: String = "channelTitle",
            tags: List<String>? = null,
            categoryId: String = "categoryId"
        ) = ContentInfoVideoDto(
            publishedAt, channelId, title, description, image, channelTitle, tags, categoryId
        )

    }
}