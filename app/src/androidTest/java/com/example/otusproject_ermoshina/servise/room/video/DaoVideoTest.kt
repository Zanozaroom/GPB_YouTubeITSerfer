package com.example.otusproject_ermoshina.servise.room.video

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.otusproject_ermoshina.servise.room.CreatorEntityRoom
import com.example.otusproject_ermoshina.servise.room.MyDataBase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaoVideoTest {

    lateinit var db: MyDataBase
    lateinit var videoDao: DaoVideo

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MyDataBase::class.java
        )
            .build()
        videoDao = db.getVideoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun addToFavorite()  {
        val videoId: EntityVideo = CreatorEntityRoom.getEntityVideo()

        videoDao.addToFavorite(videoId)
        val videoDataBase = videoDao.loadVideoTestDebug()

        Assert.assertEquals(1, videoDataBase.size)
    }

    @Test
    fun deleteVideo()  {
        val videoId: EntityVideo = CreatorEntityRoom.getEntityVideo(idVideo="idVideo1")
        val videoId2: EntityVideo = CreatorEntityRoom.getEntityVideo(idVideo="idVideo2")

        videoDao.addToFavorite(videoId)
        videoDao.addToFavorite(videoId2)
        videoDao.deleteVideo(videoId2.idVideo)
        val videoDataBase = videoDao.loadVideoTestDebug()

        Assert.assertEquals(1, videoDataBase.size)
        Assert.assertEquals("idVideo1", videoDataBase[0].idVideo)
    }
}