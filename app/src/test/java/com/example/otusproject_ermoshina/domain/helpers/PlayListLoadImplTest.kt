package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.domain.repositories.*
import com.example.otusproject_ermoshina.servise.retrofit.model.ChannelPlayListResponse
import com.example.otusproject_ermoshina.servise.room.model.RoomPlayList
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

internal class PlayListLoadImplTest {

    @get: Rule
    val rule = MockKRule(this)

    val dummyCoroutineDispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var mockDataBase: RepositoryDataBase

    @MockK
    lateinit var mockNetwork: RepositoryNetwork

    @InjectMockKs()
    lateinit var testPlayListLoadImpl: PlayListLoadImpl


    @Test
    fun `firstLoadPlayList CallsNetWorkRepository GetError`() = runBlocking {
        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } answers {
            ErrorNetworkResult
        }
        val result = testPlayListLoadImpl.firstLoadPlayList("idChannel", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)
        }
        print(result)
        assertEquals(BaseViewModel.ErrorLoadingViewModel, result)
    }

    @Test
    fun `firstLoadPlayList CallsNetWorkRepository GetSuccess`() = runBlocking {
        val channelPlayListResponse = CreatorYouTubeRepository.createChannelPlayListResponse()
        val list = channelPlayListResponse.toChannelAndListVideos()
        list.listPlayList
        val ytPaging = YTPlayListPaging(
            idChannel = "channelId",
            titleChannel = "channelTitle",
            nextToken = "nextPageToken",
            listPlayList = list.listPlayList
        )
        coEvery { mockNetwork.loadChannelList(any(), any(), any()) } returns SuccessNetworkResult(
            channelPlayListResponse
        )

        val result = testPlayListLoadImpl.firstLoadPlayList("idChannel", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)
        }
        print(result)
        assertEquals(BaseViewModel.SuccessViewModel(ytPaging), result)
    }

    @Test
    fun `firstLoadPlayList CallsNetWorkRepository GetSuccessEmptyListImageData`() = runBlocking {
        val channelPlayListResponse = mockk<ChannelPlayListResponse>()
        val list = YTPlayList(
            idChannel = "",
            idList = "",
            imageList = "",
            titleListVideo = "titleListVideo",
            titleChannel = "titleChannel"
        )
        val ytPlayListPaging = YTPlayListPaging(
            idChannel="idChannel",
            titleChannel = "titleChannel",
            nextToken = "nextToken",
            listPlayList = listOf(list)
        )

        every { channelPlayListResponse.toChannelAndListVideos() } returns ytPlayListPaging
        coEvery{ mockNetwork.loadChannelList(any(), any(), any()) } answers {
            SuccessNetworkResult(channelPlayListResponse)}

        val result = testPlayListLoadImpl.firstLoadPlayList("idChannel", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)
        }
        print(result)
        assertEquals(BaseViewModel.EmptyResultViewModel, result)

    }

    @Test
    fun `firstLoadPlayList CallsNetWorkRepository GetException`() = runBlocking {
        var exceptionThrown: Boolean = false

        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } throws NetworkLoadException("NetworkLoadException")

        try {
            testPlayListLoadImpl.firstLoadPlayList("idChannel", "token", 1)
        } catch (e: Exception) {
            if (e is NetworkLoadException) {
                exceptionThrown = true
                print(e.toString())
            }
        }

        assertTrue(exceptionThrown)
    }

    @Test
    fun `firstLoadPlayList CallsNetWorkRepository ThrowsException`() = runBlocking {
        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } throws NetworkLoadException("NetworkLoadException")

        val dataLoadException = assertThrows(NetworkLoadException::class.java) {
            runBlocking {
                testPlayListLoadImpl.firstLoadPlayList("idChannel", "token", 1)
            }
        }

        assertEquals(
            "Ошибка загрузки данных NetworkLoadException",
            dataLoadException.sayException()
        )
    }

    @Test
    fun `loadMorePlayList CallsNetWorkRepository GetError`() = runBlocking {
        val ytPaging = CreatorPlayList.createPlayListPaging()
        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } answers { ErrorNetworkResult }

        val result = testPlayListLoadImpl.loadMorePlayList(ytPaging, "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)
        }
        print(result)
        assertEquals(BaseViewModel.ErrorLoadingViewModel, result)
    }

    @Test
    fun `loadMorePlayList CallsNetWorkRepository GetSuccessResultButEmptyImage`() = runBlocking {
        val mockPlayListLoadImpl =
            PlayListLoadImpl(mockNetwork, mockDataBase, dummyCoroutineDispatcher)
        val channelPlayListResponse = mockk<ChannelPlayListResponse>()

        val list = YTPlayList(
            idChannel = "",
            idList = "",
            imageList = "",
            titleListVideo = "titleListVideo",
            titleChannel = "titleChannel")

        val ytPlayListPaging = YTPlayListPaging(
            idChannel="idChannel",
            titleChannel = "titleChannel",
            nextToken = "nextToken",
            listPlayList = listOf(list))

        every { channelPlayListResponse.toChannelAndListVideos() } returns ytPlayListPaging

        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } answers { SuccessNetworkResult(channelPlayListResponse) }

        val result = mockPlayListLoadImpl.loadMorePlayList(ytPlayListPaging, "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)
        }
        print(result)
        assertEquals(BaseViewModel.NotMoreLoadingViewModel, result)
    }

    @Test
    fun `loadMorePlayList CallsNetWorkRepository GetSuccess`() = runBlocking {
        val mockPlayListLoadImpl =
            PlayListLoadImpl(mockNetwork, mockDataBase, dummyCoroutineDispatcher)

        val channelPlayListResponse = CreatorYouTubeRepository.createChannelPlayListResponse()
        val list = channelPlayListResponse.toChannelAndListVideos()
        list.listPlayList
        val ytPaging = YTPlayListPaging(
            idChannel = "channelId",
            titleChannel = "channelTitle",
            nextToken = "nextPageToken",
            listPlayList = list.listPlayList
        )
        val concatResult = CreatorPlayList.createLoadMorePlayListAndChannel(ytPaging, ytPaging)

        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } answers { SuccessNetworkResult(channelPlayListResponse) }

        val result = mockPlayListLoadImpl.loadMorePlayList(ytPaging, "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("channelId", "token", 1)
        }
        print(result)
        assertEquals(BaseViewModel.SuccessViewModel(concatResult), result)
    }

    @Test
    fun `loadMorePlayList CallsNetWorkRepository GetEmptySuccessResult`() = runBlocking {
        val ytPaging = YTPlayListPaging(
            idChannel = "channelId",
            titleChannel = "channelTitle",
            nextToken = "nextPageToken",
            listPlayList = emptyList()
        )

        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } returns EmptyNetworkResult

        val result = testPlayListLoadImpl.loadMorePlayList(ytPaging, "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("channelId", "token", 1)
        }
        print(result)
        assertEquals(BaseViewModel.NotMoreLoadingViewModel, result)
    }

    @Test
    fun `loadMorePlayList CallsNetWorkRepository GetException`() = runBlocking {
        val mockPlayListLoadImpl =
            PlayListLoadImpl(mockNetwork, mockDataBase, dummyCoroutineDispatcher)
        val ytPaging = CreatorPlayList.createPlayListPaging()
        var exceptionThrown: Boolean = false

        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } throws NetworkLoadException("NetworkLoadException")

        try {
            mockPlayListLoadImpl.loadMorePlayList(ytPaging, "token", 1)
        } catch (e: Exception) {
            if (e is NetworkLoadException) {
                exceptionThrown = true
                print(e.toString())
            }
        }

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList(ytPaging.idChannel, "token", 1)
        }
        assertTrue(exceptionThrown)
    }


    @Test
    fun `loadMorePlayList CallsNetWorkRepository ThrowsException`() = runBlocking {
        val mockPlayListLoadImpl =
            PlayListLoadImpl(mockNetwork, mockDataBase, dummyCoroutineDispatcher)
        val ytPaging = CreatorPlayList.createPlayListPaging()

        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } throws NetworkLoadException("NetworkLoadException")

        val dataLoadException = assertThrows(NetworkLoadException::class.java) {
            runBlocking {
                mockPlayListLoadImpl.loadMorePlayList(ytPaging, "token", 1)
            }
        }

        assertEquals(
            "Ошибка загрузки данных NetworkLoadException",
            dataLoadException.sayException()
        )
    }

    @Test
    fun `addToFavoritePlayList CalledRoomDataBaseOneTimes CallAddPlayListToDataBase`() =
        runBlocking {
            val list = CreatorPlayList.createYTPlayList()
            coEvery { testPlayListLoadImpl.addToFavoritePlayList(any()) } just runs
            coEvery { mockDataBase.addPlayListToFavorite(list) } answers {
                print("был вызван addToFavoritePlayList в mockDataBase")
            }

            testPlayListLoadImpl.addToFavoritePlayList(list)

            coVerify(exactly = 1) {
                mockDataBase.addPlayListToFavorite(list)
            }
        }

    @Test
    fun `addToFavoritePlayList CallDataBaseToLoadPlayList ThrowsException`() = runBlocking {
        val list = CreatorPlayList.createYTPlayList()
        coEvery {
            mockDataBase.addPlayListToFavorite(any())
        } throws DataBaseLoadException("DataBaseLoadException")

        val dataLoadException = assertThrows(DataBaseLoadException::class.java) {
            runBlocking {
                testPlayListLoadImpl.addToFavoritePlayList(list)
            }
        }

        assertEquals(
            "Ошибка загрузки данных DataBaseLoadException",
            dataLoadException.sayException()
        )
    }

    @Test
    fun `deletePlayListFromFavorite CalledRoomDataBaseOneTimes`() = runBlocking {
        val idPlayList = "idPlayList"
        coEvery { testPlayListLoadImpl.deletePlayListFromFavorite(any()) } just runs
        coEvery { mockDataBase.deletePlayList(idPlayList = "idPlayList") } answers {
            print("был вызван deletePlayList в mockDataBase")
        }

        testPlayListLoadImpl.deletePlayListFromFavorite(idPlayList)

        coVerify(exactly = 1) {
            mockDataBase.deletePlayList("idPlayList")
        }
    }

    @Test
    fun `deletePlayListFromFavorite CallDataBaseToLoadPlayList ThrowsException`() = runBlocking {
        coEvery {
            mockDataBase.deletePlayList(any())
        } throws DataBaseLoadException("DataBaseLoadException")

        val dataLoadException = assertThrows(DataBaseLoadException::class.java) {
            runBlocking {
                testPlayListLoadImpl.deletePlayListFromFavorite("idPlayList")
            }
        }

        assertEquals(
            "Ошибка загрузки данных DataBaseLoadException",
            dataLoadException.sayException()
        )
    }

    @Test
    fun `loadFavoritePlayList CallDataBaseToLoadPlayList ReturnFlowWithData`() = runBlocking {
        val listDataBase = listOf(
            CreatorPlayList.createRoomPlayList(),
            CreatorPlayList.createRoomPlayList()
        )
        val flowDataBase: Flow<List<RoomPlayList>> = listOf(listDataBase).asFlow()
        val listFavoritePlayList = flowDataBase.map { it.map { it.toYTPlayList() } }
        coEvery { mockDataBase.loadPlayList() } returns flowDataBase

        val result = testPlayListLoadImpl.loadFavoritePlayList()

        coVerify(exactly = 1) { mockDataBase.loadPlayList() }
        print(result.toList())
        assertEquals(listFavoritePlayList.toList(), result.toList())
    }

    @Test
    fun `loadFavoritePlayList CallDataBaseToLoadPlayList GetException`() = runBlocking {
        var exceptionThrown: Boolean = false
        coEvery { mockDataBase.loadPlayList() } throws DataBaseLoadException("DataBaseLoadException")

        try {
            testPlayListLoadImpl.loadFavoritePlayList()
        } catch (e: Exception) {
            if (e is DataBaseLoadException) {
                exceptionThrown = true
                print(e.toString())
            }
        }

        coVerify(exactly = 1) { mockDataBase.loadPlayList() }
        assertTrue(exceptionThrown)
    }

    @Test
    fun `loadFavoritePlayList CallDataBaseToLoadPlayList ThrowsException`() = runBlocking {
        coEvery {
            mockDataBase.loadPlayList()
        } throws DataBaseLoadException("DataBaseLoadException")

        val dataLoadException = assertThrows(DataBaseLoadException::class.java) {
            runBlocking {
                testPlayListLoadImpl.loadFavoritePlayList()
            }
        }

        assertEquals(
            "Ошибка загрузки данных DataBaseLoadException",
            dataLoadException.sayException()
        )
    }
}
