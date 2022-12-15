package com.example.otusproject_ermoshina.base
interface Channels
data class Channel(
    val idChannel: String
): Channels
data class PlayListOfChannel(
    val idList: String,
    val imageList: String,
    val titleListVideo: String,
    val nextToken: String
): Channels
data class ChannelAndListVideos(
    val idChannel: String,
    val titleChannel: String,
    val nextToken: String,
    val listVideos: List<PlayListOfChannel>
): Channels{
    fun toVideosChannel(): List<PlayListOfChannel> {
        return listVideos.map {
            PlayListOfChannel(
                idList = it.idList,
                imageList = it.imageList,
                titleListVideo = it.titleListVideo,
                nextToken = nextToken
            )
        }
    }
}