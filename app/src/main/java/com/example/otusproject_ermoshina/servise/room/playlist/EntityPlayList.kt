package com.example.otusproject_ermoshina.servise.room.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.otusproject_ermoshina.servise.room.model.RoomPlayList

@Entity(
    tableName = "playlist")
data class EntityPlayList (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_playlist") val idPlayList: String,
    @ColumnInfo(name = "id_channel") val idChannel: String,
    val image: String,
    @ColumnInfo(name = "title_playlist") val titlePlayList:String,
    @ColumnInfo(name = "title_channel") val titleChannel:String
    ) {
        fun toPlayList() = RoomPlayList (idChannel,idPlayList,image,titlePlayList,titleChannel)
    }
