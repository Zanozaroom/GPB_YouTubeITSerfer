package com.example.otusproject_ermoshina.sources.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.otusproject_ermoshina.sources.room.channel.DaoChannels
import com.example.otusproject_ermoshina.sources.room.channel.EntityChannels
import com.example.otusproject_ermoshina.sources.room.playlist.DaoPlayList
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayList
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayListWithoutChannel
import com.example.otusproject_ermoshina.sources.room.query.DaoQuery
import com.example.otusproject_ermoshina.sources.room.query.EntityStartQuery
import com.example.otusproject_ermoshina.sources.room.video.DaoVideo
import com.example.otusproject_ermoshina.sources.room.video.EntityVideo

@Database(
    entities = [
        EntityChannels ::class,
        EntityPlayList::class,
        EntityVideo::class,
        EntityStartQuery::class,
        EntityPlayListWithoutChannel::class],
    version = 1,
    exportSchema = true
)
abstract class MyDataBase: RoomDatabase(){
    abstract fun getChannelsDAO(): DaoChannels
    abstract fun getPlayListDao(): DaoPlayList
    abstract fun getQueryDao(): DaoQuery
    abstract fun getVideoDao(): DaoVideo
}