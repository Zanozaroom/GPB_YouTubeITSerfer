package com.example.otusproject_ermoshina.sources.repositories

import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.room.query.DaoQuery
import com.example.otusproject_ermoshina.sources.room.model.RoomQueryApp
import com.example.otusproject_ermoshina.sources.room.playlist.DaoPlayList
import com.example.otusproject_ermoshina.sources.room.playlist.EntityPlayList
import com.example.otusproject_ermoshina.sources.room.video.DaoVideo
import com.example.otusproject_ermoshina.sources.room.video.EntityVideo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryRoom @Inject constructor(
    private val playListDao: DaoPlayList,
    private val queryDao: DaoQuery,
    private val videoDao: DaoVideo,
    private val coroutineDispatcher: CoroutineDispatcher
) : RepositoryDataBase {


    override fun loadPlayList() =
                playListDao.loadPlayList().map { entityList ->
                    entityList.map { it.toPlayList() }
                }

    override suspend fun addPlayListToFavorite(playList:YTPlayList) {
        withContext(coroutineDispatcher) {
            try {
                val list = toEntityPlayList(playList)
                playListDao.addToFavoriteWithoutChannel(list)
            }catch (e:Exception){
                throw DataBaseLoadException("Ошибка addPlayListToFavorite $e")
            }

        }
    }


    override suspend fun deletePlayList(playList:YTPlayList) {
        withContext(coroutineDispatcher) {
            try {
                val list = toEntityPlayList(playList)
                playListDao.deletePlayList(list)
            }catch (e:Exception){
                throw DataBaseLoadException("Ошибка deletePlayList $e")
            }
        }
    }

    override suspend fun addVideoToFavorite(video: YTVideo) {
        withContext(coroutineDispatcher) {
            try {
                videoDao.addToFavorite(toEntityVideo(video))
            } catch (e: Exception) {
                throw DataBaseLoadException("Ошибка addVideoToFavorite $e")
            }
        }
    }
    override suspend fun deleteVideo(video: YTVideo)  {
        withContext(coroutineDispatcher) {
            try {
            videoDao.deleteVideo(toEntityVideo(video))
        }catch (e: Exception) {
                throw DataBaseLoadException("Ошибка deleteVideo $e")
            }
        }
    }

    override fun loadVideo(): Flow<List<YTVideo>> =
            videoDao.loadVideo().map {
                it.map { it.toVideo().toYTVideo() }
            }.flowOn(coroutineDispatcher)


    override suspend fun loadQuery(): List<RoomQueryApp> = withContext(Dispatchers.IO) {
        try{
        queryDao.loadQueryApp().map { entityQuery -> entityQuery.toRoomQueryApp() }
    }catch (e: Exception) {
            throw DataBaseLoadException("Ошибка loadQuery $e")
        }
    }

    private fun toEntityVideo(video: YTVideo) = EntityVideo(
        idVideo = video.idVideo,
        image =  video.image,
        title = video.title,
        publishedAt = video.publishedAt,
        description = video.description,
        channelTitle = video.channelTitle,
        channelId = video.channelId,
        viewCount = video.viewCount,
        likeCount = video.likeCount
    )

    private fun toEntityPlayList(playList: YTPlayList) = EntityPlayList(
        idChannel = playList.idChannel,
        idPlayList = playList.idList,
        image = playList.imageList,
        titlePlayList = playList.titleListVideo,
        titleChannel = playList.titleChannel
    )

}






