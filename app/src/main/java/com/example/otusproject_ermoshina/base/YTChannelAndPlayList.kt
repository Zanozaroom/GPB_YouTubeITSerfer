package com.example.otusproject_ermoshina.base



data class YTChannelAndPlayList(
    val idChannel: String,
    val titleChannel: String,
    val nextToken: String,
    val listVideos: List<YTPlayList>
)
{
    fun toVideosChannel(): List<YTPlayList> {
        return listVideos.map {
            YTPlayList(
                idChannel = idChannel,
                idList = it.idList,
                imageList = it.imageList,
                titleListVideo = it.titleListVideo,
                titleChannel = it.titleChannel
            )
        }
    }
}