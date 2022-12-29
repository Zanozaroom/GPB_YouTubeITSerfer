package com.example.otusproject_ermoshina.servise.room.query

import androidx.room.*
import com.example.otusproject_ermoshina.servise.room.model.RoomQueryApp

@Entity(tableName = "query_app")
class EntityStartQuery (
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val query: String,
    @ColumnInfo(name = "title_query") val titleQuery: String
)
{
    fun toRoomQueryApp() = RoomQueryApp(
        idQueryApp = id,
        query = query,
        titleQuery = titleQuery
    )

}