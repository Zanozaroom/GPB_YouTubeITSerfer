package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.*
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearchResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearchResponse.SearchContentDto
import com.example.otusproject_ermoshina.servise.room.model.RoomQueryApp

class CreatorSearch {
    companion object {
        fun createModelSearch(
            kind: String = "kind",
            nextPageToken: String = "nextPageToken",
            resultSearchAllData: List<ModelSearchResponse.ResultSearchAllDataDto> = listOf(
                createResultSearchAllData(),
                createResultSearchAllData()
            )
        ): ModelSearchResponse {
            return ModelSearchResponse(
                kind,
                nextPageToken,
                resultSearchAllData
            )
        }

        fun createResultSearchAllData(
            kind: String = "kind",
            idVideo: ModelSearchResponse.IdVideoDto = ModelSearchResponse.IdVideoDto("kind", "IdVideo"),
            searchContent: SearchContentDto = SearchContentDto()
        ): ModelSearchResponse.ResultSearchAllDataDto {
            return ModelSearchResponse.ResultSearchAllDataDto(kind, idVideo, searchContent)
        }

        fun createYTSearchPaging(getResponse: ModelSearchResponse, query: String) =
            YTSearchPaging(
                query = query,
                nextToken = getResponse.nextPageToken ?: SearchLoadImpl.TOKEN_NULL,
                listSearchVideo = getResponse.toYTSearch()
            )

        fun createRoomQueryApp(
            idQueryApp: Int = 1,
            query: String = "query",
            titleQuery: String = "titleQuery"
        ) = RoomQueryApp(idQueryApp, query, titleQuery)

        fun createYTQuery(roomQueryApp: RoomQueryApp) = YTQuery(
            roomQueryApp.idQueryApp, roomQueryApp.titleQuery, roomQueryApp.query
        )

        fun createYTMainFragmentData(query: YTQuery, searchResponse: ModelSearchResponse) =
            YTMainFragmentData(
                id = searchResponse.hashCode(),
                query = query.query,
                title = query.titleQuery,
                listResult = searchResponse.toYTSearch()
            )

    }
}