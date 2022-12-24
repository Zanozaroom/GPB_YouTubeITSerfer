package com.example.otusproject_ermoshina.sources.room.model

import com.example.otusproject_ermoshina.base.YTQuery

data class RoomQueryApp(val idQueryApp: Int, val query: String, val titleQuery: String) {
    fun toYTQuery() = YTQuery(
        idQuery = idQueryApp,
        titleQuery = titleQuery,
        query = query
    )
}
