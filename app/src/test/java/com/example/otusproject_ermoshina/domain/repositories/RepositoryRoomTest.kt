package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.helpers.CreatorPlayList
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.servise.room.model.RoomQueryApp
import com.example.otusproject_ermoshina.servise.room.playlist.DaoPlayList
import com.example.otusproject_ermoshina.servise.room.query.DaoQuery
import com.example.otusproject_ermoshina.servise.room.query.EntityStartQuery
import com.example.otusproject_ermoshina.servise.room.video.DaoVideo
import com.example.otusproject_ermoshina.servise.room.video.EntityVideo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class RepositoryRoomTest {
    val dummyCoroutineDispatcher = Dispatchers.Unconfined

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var mockPlayListDao: DaoPlayList

    @RelaxedMockK
    lateinit var mockDaoQuery: DaoQuery

    @RelaxedMockK
    lateinit var mockDaoVideo: DaoVideo

    lateinit var testRepositoryRoom: RepositoryRoom

    @Before
    fun init() {
        testRepositoryRoom =
            RepositoryRoom(mockPlayListDao, mockDaoQuery, mockDaoVideo, dummyCoroutineDispatcher)
    }

    @Test
    fun `addPlayListToFavorite CallsToRoomDaoPlayList CheckThatWasSetRightArguments`() =
        runBlocking {
            val playList = CreatorPlayList.createYTPlayList()
            val list = CreatorRepositoryRoom.toEntityPlayList(playList)

            coEvery { mockPlayListDao.addToFavorite(any()) } just runs

            testRepositoryRoom.addPlayListToFavorite(playList)

            coVerify(exactly = 1) {
                mockPlayListDao.addToFavorite(list)
            }

        }

    @Test
    fun `addPlayListToFavorite CallsToRoomDaoPlayList CheckThatWasThrowRightException`() =
        runBlocking {
            val playList = CreatorPlayList.createYTPlayList()
            var isThrowException = false

            coEvery { mockPlayListDao.addToFavorite(any()) } throws Exception("mockPlayListDaoException")
            try {
                testRepositoryRoom.addPlayListToFavorite(playList)
            } catch (e: Exception) {
                if (e is DataBaseLoadException) {
                    print(e)
                    isThrowException = true
                }
            }
            assertTrue(isThrowException)
        }


    @Test
    fun `deletePlayList CallsToRoomDaoPlayList CheckThatWasSetRightArguments`() = runBlocking {
        val idPlayList = "idPlayList"

        coEvery { mockPlayListDao.deletePlayList(any()) } just runs

        testRepositoryRoom.deletePlayList(idPlayList)

        coVerify(exactly = 1) {
            mockPlayListDao.deletePlayList(idPlayList)
        }
    }

    @Test
    fun `deletePlayList CallsToRoomDaoPlayList CheckThatWasThrowRightException`() = runBlocking {
        val idPlayList = "idPlayList"
        var isThrowException = false

        coEvery { mockPlayListDao.deletePlayList(any()) } throws Exception("mockPlayListDaoException")
        try {
            testRepositoryRoom.deletePlayList(idPlayList)
        } catch (e: Exception) {
            if (e is DataBaseLoadException) {
                print(e)
                isThrowException = true
            }
        }
        assertTrue(isThrowException)
    }


    @Test
    fun `addVideoToFavorite CallsToRoomDaoVideo CheckThatWasSetRightArguments`() = runBlocking {
        val video = CreatorRepositoryRoom.createYTVideo()
        val entityVideo = CreatorRepositoryRoom.toEntityVideo(video)
        coEvery { mockDaoVideo.addToFavorite(any()) } just runs

        testRepositoryRoom.addVideoToFavorite(video)

        coVerify(exactly = 1) {
            mockDaoVideo.addToFavorite(entityVideo)
        }
    }

    @Test
    fun `addVideoToFavorite CallsToRoomDaoVideo CheckThatWasThrowRightException`() = runBlocking {
        val video = CreatorRepositoryRoom.createYTVideo()
        var isThrowException = false
        coEvery { mockDaoVideo.addToFavorite(any()) } throws Exception("mockDaoVideoException")

        try {
            testRepositoryRoom.addVideoToFavorite(video)
        } catch (e: Exception) {
            if (e is DataBaseLoadException) {
                print(e)
                isThrowException = true
            }
        }
        assertTrue(isThrowException)
    }

    @Test
    fun `deleteVideo CallsToRoomDaoVideo CheckThatWasSetRightArguments`() = runBlocking {
        val video = CreatorRepositoryRoom.createYTVideo()
        val idVideo = video.idVideo
        coEvery { mockDaoVideo.deleteVideo(any()) } just runs

        testRepositoryRoom.deleteVideo(video)

        coVerify(exactly = 1) {
            mockDaoVideo.deleteVideo(idVideo)
        }
    }

    @Test
    fun `deleteVideo CallsToRoomDaoVideo CheckThatWasThrowRightException`() = runBlocking {
        val video = CreatorRepositoryRoom.createYTVideo()
        var isThrowException = false
        coEvery { mockDaoVideo.deleteVideo(any()) } throws Exception("mockDaoVideoException")

        try {
            testRepositoryRoom.deleteVideo(video)
        } catch (e: Exception) {
            if (e is DataBaseLoadException) {
                print(e)
                isThrowException = true
            }
        }
        assertTrue(isThrowException)

    }

    @Test
    fun `loadVideo CallsToRoomDaoVideo CheckWasRightFlow`() = runBlocking {
        val videoOne = CreatorRepositoryRoom.createYTVideo(idVideo = "videoOne")
        val videoTwo = CreatorRepositoryRoom.createYTVideo(idVideo = "videoTwo")
        val videoThree = CreatorRepositoryRoom.createYTVideo(idVideo = "videoThree")
        val listOfVideo = listOf(videoOne, videoTwo, videoThree)
        val flowFromRepository: Flow<List<YTVideo>> = listOf(listOfVideo).asFlow()

        val entityVideoOne = CreatorRepositoryRoom.toEntityVideo(videoOne)
        val entityVideoTwo = CreatorRepositoryRoom.toEntityVideo(videoTwo)
        val entityVideoThree = CreatorRepositoryRoom.toEntityVideo(videoThree)
        val listOfEntityVideo = listOf(entityVideoOne, entityVideoTwo, entityVideoThree)
        val flowFromDataBase: Flow<List<EntityVideo>> = listOf(listOfEntityVideo).asFlow()

        coEvery { mockDaoVideo.loadVideo() } returns flowFromDataBase

        val result = testRepositoryRoom.loadVideo()

        assertEquals(flowFromRepository.toList(), result.toList())
    }

    @Test
    fun `loadQuery CallsToRoomDaoQuery CheckThatWasThrowRightException`() = runBlocking{
        var isThrowException = false

        coEvery { mockDaoQuery.loadQueryApp() } throws Exception()
        try{
            testRepositoryRoom.loadQuery()
        }catch (e: Exception) {
            if (e is DataBaseLoadException) {
                print(e)
                isThrowException = true
            }
        }
        assertTrue(isThrowException)
    }

    @Test
    fun `loadQuery CallsToRoomDaoQuery GetRightDataFromDataBase`() = runBlocking{
        val dataFromDataBase: List<EntityStartQuery> = listOf(
            CreatorRepositoryRoom.createEntityStartQuery(id=1),
            CreatorRepositoryRoom.createEntityStartQuery(id=2),
            CreatorRepositoryRoom.createEntityStartQuery(id=3))
        val dataFromRepositoryRoom:List<RoomQueryApp> = dataFromDataBase.map { it.toRoomQueryApp()}

        coEvery { mockDaoQuery.loadQueryApp() } returns dataFromDataBase

        val result = testRepositoryRoom.loadQuery()

        assertEquals(dataFromRepositoryRoom, result)
        coVerify(exactly = 1) { mockDaoQuery.loadQueryApp() }
    }
}
