package com.example.otusproject_ermoshina.domain.helpers

import android.database.sqlite.SQLiteException
import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.RepositoryDataBase
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
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
    fun firstLoadPlayListError() = runBlocking {
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
    fun firstLoadPlayListSuccess() = runBlocking {
        val ytPaging = CreatorPlayList.createPlayListPaging()
        coEvery {mockNetwork.loadChannelList(any(), any(), any())} answers {SuccessNetworkResult(ytPaging)}

        val result = testPlayListLoadImpl.firstLoadPlayList("idChannel", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)}
        print(result)
        assertEquals(BaseViewModel.SuccessViewModel(ytPaging), result)
    }

    @Test
    fun firstLoadPlayListSuccessEmpty() = runBlocking {
        val ytPaging =CreatorPlayList.createPlayListPaging(listPlayList = emptyList())
        coEvery {mockNetwork.loadChannelList(any(), any(), any())} answers {
            SuccessNetworkResult(ytPaging)
        }

        val result = testPlayListLoadImpl.firstLoadPlayList("idChannel", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)}
        print(result)
        assertEquals(BaseViewModel.EmptyResultViewModel, result)

    }

    @Test
    fun loadMorePlayListError() = runBlocking {
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
    fun loadMorePlayListSuccess() = runBlocking {
        val mockPlayListLoadImpl = PlayListLoadImpl(mockNetwork, mockDataBase, dummyCoroutineDispatcher)
        val ytPaging =CreatorPlayList.createPlayListPaging()
        val concatResult = CreatorPlayList.createLoadMorePlayListAndChannel(ytPaging, ytPaging)
        coEvery {
            mockNetwork.loadChannelList(any(), any(), any())
        } answers { SuccessNetworkResult(ytPaging) }

        val result = mockPlayListLoadImpl.loadMorePlayList(ytPaging, "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.loadChannelList("idChannel", "token", 1)}
        print(result)
        assertEquals(BaseViewModel.SuccessViewModel(concatResult), result)
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
    fun deletePlayListFromFavorite() = runBlocking{
    val idPlayList = "idPlayList"
        coEvery { testPlayListLoadImpl.deletePlayListFromFavorite(any()) } just runs
        coEvery { mockDataBase.deletePlayList(idPlayList="idPlayList") } answers {
            print("был вызван deletePlayList в mockDataBase")}

        testPlayListLoadImpl.deletePlayListFromFavorite(idPlayList)

        coVerify(exactly = 1) {
            mockDataBase.deletePlayList("idPlayList")
        }
    }

    @Test
    fun `loadFavoritePlayList CallDataBaseToLoadPlayList ReturnFlowWithData`() = runBlocking {
        val listDataBase = listOf(
            CreatorPlayList.createRoomPlayList(),
            CreatorPlayList.createRoomPlayList())
        val flowDataBase: Flow<List<RoomPlayList>> = listOf(listDataBase).asFlow()
        val listFavoritePlayList = flowDataBase.map { it.map{it.toYTPlayList() }}
        coEvery { mockDataBase.loadPlayList() } returns flowDataBase

       val result = testPlayListLoadImpl.loadFavoritePlayList()

        coVerify(exactly = 1) {mockDataBase.loadPlayList()}
        print(result.toList())
        assertEquals(listFavoritePlayList.toList(), result.toList())
    }

    @Test
    fun `loadFavoritePlayList CallDataBaseToLoadPlayList ReturnException`() = runBlocking {
        var exceptionThrown: Boolean = false
        coEvery { mockDataBase.loadPlayList() }. throws (SQLiteException ("Room exception"))

        try{
             testPlayListLoadImpl.loadFavoritePlayList()
        }catch (e:Exception){
        if (e is SQLiteException) {
            exceptionThrown= true
            print(e.toString())
        }
        }

        coVerify(exactly = 1) {mockDataBase.loadPlayList()}
        assertTrue(exceptionThrown)
    }

}
