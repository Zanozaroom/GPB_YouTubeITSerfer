package com.example.otusproject_ermoshina.sources.room.query

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DaoQuery {
    @Query("SELECT * FROM query_app")
    suspend fun loadQueryApp(): List<EntityStartQuery>

}