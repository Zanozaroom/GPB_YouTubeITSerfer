package com.example.otusproject_ermoshina.sources.repositories

import androidx.room.Transaction
import com.example.otusproject_ermoshina.base.YTChannelAndListVideos
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.room.channel.DaoChannels
import com.example.otusproject_ermoshina.sources.room.channel.EntityChannels
import com.example.otusproject_ermoshina.sources.room.query.DaoQuery
import com.example.otusproject_ermoshina.sources.room.model.RoomQueryApp
import com.example.otusproject_ermoshina.sources.room.playlist.DaoPlayList
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayList
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayListWithoutChannel
import com.example.otusproject_ermoshina.sources.room.video.DaoVideo
import com.example.otusproject_ermoshina.sources.room.video.EntityVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryRoom @Inject constructor(
    private val playListDao: DaoPlayList,
    private val queryDao: DaoQuery,
    private val videoDao: DaoVideo
) : RepositoryDataBase {


    override suspend fun loadPlayList() =
        withContext(Dispatchers.IO) {
        playListDao.loadPlayList().map { entityPlayList ->
            entityPlayList.toRoomPlayList()
        }
    }
    override suspend fun addPlayListToFavorite(playList:YTPlayListOfChannel) {
        withContext(Dispatchers.IO) {
            val list = toEntityPlayList(playList)
            playListDao.addToFavoriteWithoutChannel(list)
        }
    }


    override suspend fun deletePlayList(playList:YTPlayListOfChannel) {
        withContext(Dispatchers.IO) {
            val list = toEntityPlayList(playList)
            playListDao.deletePlayList(list)
        }
    }

    override suspend fun addVideoToFavorite(video: YTVideo) {
        withContext(Dispatchers.IO) {
            videoDao.addToFavorite(toEntityVideo(video))
        }
    }
    override suspend fun deleteVideo(video: YTVideo)  {
        withContext(Dispatchers.IO) {
            videoDao.deleteVideo(toEntityVideo(video))
        }
    }

    override suspend fun loadVideo(): List<YTVideo> =
        withContext(Dispatchers.IO) {
            videoDao.loadVideo().map { it.toVideo().toYTVideo() }
        }


    override suspend fun loadQuery(): List<RoomQueryApp> = withContext(Dispatchers.IO) {
        queryDao.loadQueryApp().map { entityQuery -> entityQuery.toRoomQueryApp() }
    }

    fun toEntityVideo(video: YTVideo) = EntityVideo(
        idVideo = video.idVideo,
        title = video.title,
        description = video.description,
        channelTitle = video.channelTitle,
        viewCount = video.viewCount,
        likeCount = video.likeCount
    )

    fun toEntityPlayList( playList: YTPlayListOfChannel) = EntityPlayListWithoutChannel(
        idChannel = playList.idChannel,
        idPlayList = playList.idList,
        image = playList.imageList,
        titlePlayList = playList.titleListVideo,
        titleChannel = playList.titleChannel
    )

}






