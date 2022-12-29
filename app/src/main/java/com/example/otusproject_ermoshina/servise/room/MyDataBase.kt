package com.example.otusproject_ermoshina.servise.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.otusproject_ermoshina.servise.room.playlist.DaoPlayList
import com.example.otusproject_ermoshina.servise.room.playlist.EntityPlayList
import com.example.otusproject_ermoshina.servise.room.query.DaoQuery
import com.example.otusproject_ermoshina.servise.room.query.EntityStartQuery
import com.example.otusproject_ermoshina.servise.room.video.DaoVideo
import com.example.otusproject_ermoshina.servise.room.video.EntityVideo

@Database(
    entities = [
        EntityStartQuery::class,
        EntityPlayList::class,
        EntityVideo::class],
    version = 1,
    exportSchema = true
)
abstract class MyDataBase : RoomDatabase() {
    abstract fun getPlayListDao(): DaoPlayList
    abstract fun getQueryDao(): DaoQuery
    abstract fun getVideoDao(): DaoVideo
}