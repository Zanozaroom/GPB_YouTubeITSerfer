package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class VideoListLoadImplTest {

    val dummyCoroutineDispatcher = Dispatchers.Unconfined

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var mockVideoHelper: VideoLoad

    @RelaxedMockK
    lateinit var mockNetwork: RepositoryNetwork

    lateinit var testVideoListLoadImpl: VideoListLoadImpl

    @Before
    fun init() {
        testVideoListLoadImpl = VideoListLoadImpl(mockNetwork, mockVideoHelper, dummyCoroutineDispatcher)
    }

    @Test
    fun `testGetVideoList CallsNetwork CheckThatWasSetRightArguments`() = runBlocking {
        val netWorkResult = CreatorVideoList.createModelLoadListVideos()

        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns SuccessNetworkResult(netWorkResult)

        testVideoListLoadImpl.getVideoList("playListId", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.getListVideos("playListId", "token", 1)
        }
    }

    @Test
    fun `testGetVideoList CallsNetwork GetSuccessNetworkResult`() = runBlocking {
        val netWorkResult = CreatorVideoList.createModelLoadListVideos()
        val finalResult = CreatorVideoList.createYTVideoList(netWorkResult)

        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns SuccessNetworkResult(netWorkResult)

        val result = testVideoListLoadImpl.getVideoList("playListId", "token", 1)

        assertEquals(BaseViewModel.SuccessViewModel(finalResult), result)
    }

    @Test
    fun `testGetVideoList CallsNetwork GetSuccessNetworkResultButEmptyImage`() = runBlocking {
        val netWorkResult = mockk<ModelLoadListVideosResponse>()
        val ytVideoList = CreatorVideoList.createYTVideoList(image = "")

        every {netWorkResult.toVideoList()} returns listOf(ytVideoList)
        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns SuccessNetworkResult(netWorkResult)

        val result = testVideoListLoadImpl.getVideoList("playListId", "token", 1)

        assertEquals(BaseViewModel.EmptyResultViewModel, result)
    }

    @Test
    fun `testGetVideoList CallsNetwork GetEmptyNetworkResult`() = runBlocking {
        val netWorkResult = CreatorVideoList.createModelLoadListVideos(contentContainer = emptyList())

        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns SuccessNetworkResult(netWorkResult)

        val result = testVideoListLoadImpl.getVideoList("playListId", "token", 1)

        assertEquals(BaseViewModel.EmptyResultViewModel, result)
    }

    @Test
    fun `testGetVideoList CallsNetwork GetErrorNetworkResult`() = runBlocking {
        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns ErrorNetworkResult

        val result = testVideoListLoadImpl.getVideoList("playListId", "token", 1)

        assertEquals(BaseViewModel.ErrorLoadingViewModel, result)
    }

    @Test
    fun `testGetVideoList CallsNetWorkRepository GetException`() = runBlocking {
        var exceptionThrown: Boolean = false

        coEvery {
            mockNetwork.getListVideos(any(),any(),any())
        } throws NetworkLoadException("NetworkLoadException")

        try {
            testVideoListLoadImpl.getVideoList("playListId", "token", 1)
        } catch (e: Exception) {
            if (e is NetworkLoadException) {
                exceptionThrown = true
                print(e.toString())
            }
        }

        Assert.assertTrue(exceptionThrown)
    }

    @Test
    fun `testGetVideoList CallsNetWorkRepository ThrowsException`() = runBlocking {
        coEvery {
            mockNetwork.getListVideos(any(),any(),any())
        } throws NetworkLoadException("NetworkLoadException")

        val dataLoadException = Assert.assertThrows(NetworkLoadException::class.java) {
            runBlocking {
                testVideoListLoadImpl.getVideoList("playListId", "token", 1)
            }
        }

        assertEquals(
            "Ошибка загрузки данных NetworkLoadException",
            dataLoadException.sayException()
        )
    }

    @Test
    fun `testGetLoadMoreVideoList CallsNetwork GetErrorNetworkResult`() = runBlocking {
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()
        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns ErrorNetworkResult

        val result = testVideoListLoadImpl.getLoadMoreVideoList(ytVideoListPaging, 1)

        assertEquals(BaseViewModel.ErrorLoadingViewModel, result)
    }

    @Test
    fun `testGetLoadMoreVideoList CallsNetwork GetEmptyNetworkResult`() = runBlocking {
        val netWorkResult = CreatorVideoList.createModelLoadListVideos(contentContainer = emptyList())
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()

        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns SuccessNetworkResult(netWorkResult)

        val result = testVideoListLoadImpl.getLoadMoreVideoList(ytVideoListPaging, 1)

        assertEquals(BaseViewModel.NotMoreLoadingViewModel, result)
    }

    @Test
    fun `testGetLoadMoreVideoList CallsNetwork GetSuccessNetworkResult`() = runBlocking {
        val netWorkResult = CreatorVideoList.createModelLoadListVideos()
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()
        val finalResult = CreatorVideoList.createYTVideoListPaging(ytVideoListPaging, netWorkResult)

        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns SuccessNetworkResult(netWorkResult)

        val result = testVideoListLoadImpl.getLoadMoreVideoList(ytVideoListPaging, 1)

        assertEquals(BaseViewModel.SuccessViewModel(finalResult), result)
    }
    @Test
    fun `testGetLoadMoreVideoList CallsNetwork GetSuccessNetworkResultButEmptyImage`() = runBlocking {
        val netWorkResult = mockk<ModelLoadListVideosResponse>()
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()
        val ytVideoList = CreatorVideoList.createYTVideoList(image = "")

        every {netWorkResult.toVideoList()} returns listOf(ytVideoList)
        coEvery { mockNetwork.getListVideos(any(),any(),any())
        } returns SuccessNetworkResult(netWorkResult)

        val result = testVideoListLoadImpl.getLoadMoreVideoList(ytVideoListPaging,  1)

        assertEquals(BaseViewModel.NotMoreLoadingViewModel, result)
    }

    @Test
    fun `testGetLoadMoreVideoList CallsNetWorkRepository GetException`() = runBlocking {
        var exceptionThrown: Boolean = false
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()

        coEvery {
            mockNetwork.getListVideos(any(),any(),any())
        } throws NetworkLoadException("NetworkLoadException")

        try {
            testVideoListLoadImpl.getLoadMoreVideoList(ytVideoListPaging, 1)
        } catch (e: Exception) {
            if (e is NetworkLoadException) {
                exceptionThrown = true
                print(e.toString())
            }
        }

        Assert.assertTrue(exceptionThrown)
    }


    @Test
    fun `testGetLoadMoreVideoList CallsNetWorkRepository ThrowsException`() = runBlocking {
        val ytVideoListPaging = CreatorVideoList.createFirstYTVideoListPaging()

        coEvery {
            mockNetwork.getListVideos(any(),any(),any())
        } throws NetworkLoadException("NetworkLoadException")

        val dataLoadException = Assert.assertThrows(NetworkLoadException::class.java) {
            runBlocking {
                testVideoListLoadImpl.getLoadMoreVideoList(ytVideoListPaging, 1)
            }
        }

        assertEquals(
            "Ошибка загрузки данных NetworkLoadException",
            dataLoadException.sayException()
        )
    }
}