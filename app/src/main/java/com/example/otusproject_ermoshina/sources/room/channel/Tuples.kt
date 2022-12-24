package com.example.otusproject_ermoshina.sources.room.channel

import androidx.room.Embedded
import androidx.room.Relation
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayList

class Tuples

data class ChannelsAndPlayList(
    @Embedded val channels: EntityChannels,

    @Relation(
        parentColumn = "id_channel",
        entityColumn = "id_play_list",
        entity = EntityPlayList::class
    )
    val playList: List<EntityPlayList>
)