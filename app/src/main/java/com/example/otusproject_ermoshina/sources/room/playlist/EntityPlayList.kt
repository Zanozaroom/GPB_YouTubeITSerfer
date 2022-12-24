package com.example.otusproject_ermoshina.sources.room.playlist

import androidx.room.*
import com.example.otusproject_ermoshina.sources.room.channel.EntityChannels
import com.example.otusproject_ermoshina.sources.room.model.RoomPlayList

@Entity(
    tableName = "play_lists",
    indices = [Index ("id_play_list"), Index("id_chn")],
    foreignKeys = [ForeignKey(
        entity = EntityChannels::class,
        parentColumns = ["id_channel"],
        childColumns = ["id_play_list"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)])
data class EntityPlayList (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_chn") val idChannel: String,
    @ColumnInfo(name = "id_play_list") val idPlayList: String,
    val image: String,
    @ColumnInfo(name = "title_playlist") val titlePlayList:String,
    @ColumnInfo(name = "title_channel") val titleChannel:String
) {
    fun toRoomPlayList() = RoomPlayList (id,idChannel,idPlayList,image,titlePlayList,titleChannel)
}
