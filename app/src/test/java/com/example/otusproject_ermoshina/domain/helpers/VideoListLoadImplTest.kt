package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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
}