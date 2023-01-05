package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.RepositoryDataBase
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class VideoLoadImplTest {

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var mockDataBase: RepositoryDataBase

    @RelaxedMockK
    lateinit var mockNetwork: RepositoryNetwork

    @InjectMockKs
    lateinit var testSearchLoadImp: VideoLoadImpl

    @Test
    fun `loadingYTVideo CallsNetwork CheckThatWasSetRightArguments`() = runBlocking {
        coEvery { mockNetwork.loadOneVideo(any())
        } returns SuccessNetworkResult(CreatorVideo.createModelLoadVideo())

        testSearchLoadImp.loadingYTVideo("idVideo")

        coVerify(exactly = 1) {
            mockNetwork.loadOneVideo("idVideo")
        }
    }

    @Test
    fun `loadingYTVideo CallsNetwork GetSuccessResult`() = runBlocking {
        val resultNetwork = CreatorVideo.createModelLoadVideo()
        val finalResult = resultNetwork.toVideo()
        coEvery { mockNetwork.loadOneVideo(any())
        } returns SuccessNetworkResult(resultNetwork)

        val result = testSearchLoadImp.loadingYTVideo("idVideo")

        assertEquals(BaseViewModel.SuccessViewModel(finalResult), result)
    }

    @Test
    fun `loadingYTVideo CallsNetwork GetErrorResult`() = runBlocking {
        coEvery { mockNetwork.loadOneVideo(any())
        } returns ErrorNetworkResult

        val result = testSearchLoadImp.loadingYTVideo("idVideo")

        assertEquals(BaseViewModel.ErrorLoadingViewModel, result)
    }

    @Test
    fun `loadingYTVideo CallsNetWorkRepository GetException`() = runBlocking {
        var exceptionThrown: Boolean = false

        coEvery {
            mockNetwork.loadOneVideo(any())
        } throws NetworkLoadException("NetworkLoadException")

        try {
            testSearchLoadImp.loadingYTVideo("idVideo")
        } catch (e: Exception) {
            if (e is NetworkLoadException) {
                exceptionThrown = true
                print(e.toString())
            }
        }

        Assert.assertTrue(exceptionThrown)
    }


    @Test
    fun `loadingYTVideo CallsNetWorkRepository ThrowsException`() = runBlocking {
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()

        coEvery {
            mockNetwork.loadOneVideo(any())
        } throws NetworkLoadException("NetworkLoadException")

        val dataLoadException = Assert.assertThrows(NetworkLoadException::class.java) {
            runBlocking {
                testSearchLoadImp.loadingYTVideo("idVideo")
            }
        }

        assertEquals(
            "Ошибка загрузки данных NetworkLoadException",
            dataLoadException.sayException()
        )
    }

    @Test
    fun `loadingYTVideoForSaving CallsNetwork GetErrorResult`() = runBlocking {
        coEvery { mockNetwork.loadOneVideo(any())
        } returns ErrorNetworkResult

        val result = testSearchLoadImp.loadingYTVideoForSaving("idVideo")

        assertEquals(null, result)
    }

    @Test
    fun `loadingYTVideoForSaving CallsNetwork GetSuccessResult`() = runBlocking {
        val resultNetwork = CreatorVideo.createModelLoadVideo()
        val finalResult = resultNetwork.toVideo()

        coEvery { mockNetwork.loadOneVideo(any())
        } returns SuccessNetworkResult(resultNetwork)

        val result = testSearchLoadImp.loadingYTVideoForSaving("idVideo")

        assertEquals(finalResult, result)
    }

    @Test
    fun `loadingYTVideoForSaving CallsNetwork GetEmptyResult`() = runBlocking {
        val resultNetwork = CreatorVideo.createModelLoadVideo(resultContentHeads = emptyList())

        coEvery { mockNetwork.loadOneVideo(any())
        } returns SuccessNetworkResult(resultNetwork)

        val result = testSearchLoadImp.loadingYTVideoForSaving("idVideo")

        assertEquals(null, result)
    }

    @Test
    fun `loadingYTVideoForSaving CallsNetWorkRepository GetException`() = runBlocking {
        var exceptionThrown: Boolean = false

        coEvery {
            mockNetwork.loadOneVideo(any())
        } throws NetworkLoadException("NetworkLoadException")

        try {
            testSearchLoadImp.loadingYTVideoForSaving("idVideo")
        } catch (e: Exception) {
            if (e is NetworkLoadException) {
                exceptionThrown = true
                print(e.toString())
            }
        }

        assertTrue(exceptionThrown)
    }


    @Test
    fun `loadingYTVideoForSaving CallsNetWorkRepository ThrowsException`() = runBlocking {
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()

        coEvery {
            mockNetwork.loadOneVideo(any())
        } throws NetworkLoadException("NetworkLoadException")

        val dataLoadException = Assert.assertThrows(NetworkLoadException::class.java) {
            runBlocking {
                testSearchLoadImp.loadingYTVideo("idVideo")
            }
        }

        assertEquals(
            "Ошибка загрузки данных NetworkLoadException",
            dataLoadException.sayException()
        )
    }
}