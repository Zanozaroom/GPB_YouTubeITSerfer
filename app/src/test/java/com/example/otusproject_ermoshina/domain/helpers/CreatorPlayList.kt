package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.servise.room.model.RoomPlayList

internal class CreatorPlayList {
    companion object {

        fun createYTPlayList(
            idChannel: String = "idChannel",
            idList: String = "idList",
            imageList: String = "imageList",
            titleListVideo: String = "titleListVideo",
            titleChannel: String = "titleChannel"
        ): YTPlayList {
            return YTPlayList(
                idChannel,
                idList,
                imageList,
                titleListVideo,
                titleChannel
            )
        }

        fun createRoomPlayList(
            idChannel: String = "idChannel",
            idPlayList: String = "idList",
            imageList: String = "imageList",
            titleListVideo: String = "titleListVideo",
            titleChannel: String = "titleChannel"
        ): RoomPlayList {
            return RoomPlayList(
                idChannel,
                idPlayList,
                imageList,
                titleListVideo,
                titleChannel
            )
        }

        fun createPlayListPaging(
            idChannel: String = "idChannel",
            titleChannel: String = "titleChannel",
            nextToken: String = "nextToken",
            listPlayList: List<YTPlayList> = listOf(
                createYTPlayList(),
                createYTPlayList(),
                createYTPlayList()
            )
        ): YTPlayListPaging =
            YTPlayListPaging(
                idChannel,
                titleChannel,
                nextToken,
                listPlayList
            )

        fun createLoadMorePlayListAndChannel(
            oldData: YTPlayListPaging,
            newData: YTPlayListPaging
        ): YTPlayListPaging {
            val concatPlayList = mutableListOf<YTPlayList>()
            concatPlayList.addAll(oldData.listPlayList)
            concatPlayList.addAll(newData.listPlayList)

            return YTPlayListPaging(
                newData.idChannel,
                newData.titleChannel,
                newData.nextToken,
                concatPlayList
            )
        }

    }

}