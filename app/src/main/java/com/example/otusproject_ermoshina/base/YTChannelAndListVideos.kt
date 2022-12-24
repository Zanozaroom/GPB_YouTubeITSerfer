package com.example.otusproject_ermoshina.base

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class YTPlayListOfChannel(
    val idChannel: String,
    val idList: String,
    val imageList: String,
    val titleListVideo: String,
    val titleChannel:String
)

data class YTChannelAndListVideos(
    val idChannel: String,
    val titleChannel: String,
    val nextToken: String,
    val listVideos: List<YTPlayListOfChannel>
)
{
    fun toVideosChannel(): List<YTPlayListOfChannel> {
        return listVideos.map {
            YTPlayListOfChannel(
                idChannel = idChannel,
                idList = it.idList,
                imageList = it.imageList,
                titleListVideo = it.titleListVideo,
                titleChannel = it.titleChannel
            )
        }
    }
}