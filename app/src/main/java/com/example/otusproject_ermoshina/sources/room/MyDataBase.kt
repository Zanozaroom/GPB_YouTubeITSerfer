package com.example.otusproject_ermoshina.sources.room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [EntityChannels ::class],
    version = 1,
    exportSchema = true
)
abstract class MyDataBase: RoomDatabase(){
    abstract fun getChannelsDAO(): DaoChannels
}