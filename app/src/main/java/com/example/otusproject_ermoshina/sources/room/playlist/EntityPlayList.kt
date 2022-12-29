package com.example.otusproject_ermoshina.sources.room.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.otusproject_ermoshina.sources.room.model.RoomPlayList

@Entity(
    tableName = "playlist")
data class EntityPlayList (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_channel") val idChannel: String,
    @ColumnInfo(name = "id_playlist") val idPlayList: String,
    val image: String,
    @ColumnInfo(name = "title_playlist") val titlePlayList:String,
    @ColumnInfo(name = "title_channel") val titleChannel:String
    ) {
        fun toPlayList() = RoomPlayList (id,idChannel,idPlayList,image,titlePlayList,titleChannel)
    }
