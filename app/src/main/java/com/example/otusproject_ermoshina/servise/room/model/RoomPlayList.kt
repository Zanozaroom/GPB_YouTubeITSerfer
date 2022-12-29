package com.example.otusproject_ermoshina.servise.room.model

import com.example.otusproject_ermoshina.domain.model.YTPlayList

data class RoomPlayList(
    val idChannel: String,
    val idPlayList: String,
    val imageList: String,
    val titleListVideo: String,
    val titleChannel: String
) {
    fun toYTPlayList() = YTPlayList(
        idChannel = idChannel,
        idList = idPlayList,
        imageList = imageList,
        titleListVideo = titleListVideo,
        titleChannel = titleChannel
    )
}

