package com.example.otusproject_ermoshina.sources.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.otusproject_ermoshina.base.Channel


@Entity(tableName = "channels")
data class EntityChannels(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "id_channel") val idChannel: String
)
{
    fun toChannel()= Channel(idChannel = idChannel)
}
