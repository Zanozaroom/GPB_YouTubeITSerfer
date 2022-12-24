package com.example.otusproject_ermoshina.sources.room.channel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.otusproject_ermoshina.sources.room.model.RoomChannel


@Entity(tableName = "channels", indices = [Index (value = ["id_channel"], unique = true)])
data class EntityChannels(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo(name = "id_channel") val idChannel: String,
    val title: String

)
{
    fun toChannel()= RoomChannel(idChannel,title)
}
