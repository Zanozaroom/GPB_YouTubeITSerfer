package com.example.otusproject_ermoshina.creatordata

import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging

object CreatorYTPlayListData {

    val listYTPlayList = listOf(
        createYTPlayList(idChannel = "idChannel1", idList = "idList1"),
        createYTPlayList(idChannel = "idChannel2", idList = "idList2"),
        createYTPlayList(idChannel = "idChannel3", idList = "idList3"),
        createYTPlayList(idChannel = "idChannel4", idList = "idList4"),
        createYTPlayList(idChannel = "idChannel5", idList = "idList5")
    )

    fun createYTPlayListPaging(
        idChannel: String = "idChannel",
        titleChannel: String = "titleChannel",
        nextToken: String = "nextToken",
        listPlayList: List<YTPlayList> = listYTPlayList
    ) = YTPlayListPaging(
        idChannel = idChannel,
        titleChannel = titleChannel,
        nextToken = nextToken,
        listPlayList = listPlayList
    )

    fun createYTPlayList(
        idChannel: String = "idChannel",
        idList: String = "idList",
        imageList: String = "",
        titleListVideo: String = "titleListVideo",
        titleChannel: String = "titleChannel"
    ) = YTPlayList(
        idChannel =idChannel,
        idList = idList,
        imageList = imageList,
        titleListVideo = titleListVideo,
        titleChannel = titleChannel
    )
}