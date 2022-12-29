package com.example.otusproject_ermoshina.servise.room.model

import com.example.otusproject_ermoshina.domain.model.YTQuery

data class RoomQueryApp(val idQueryApp: Int, val query: String, val titleQuery: String) {
    fun toYTQuery() = YTQuery(
        idQuery = idQueryApp,
        titleQuery = titleQuery,
        query = query
    )
}
