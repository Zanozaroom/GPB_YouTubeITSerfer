package com.example.otusproject_ermoshina.domain.helpers

import com.example.otusproject_ermoshina.domain.repositories.ErrorNetworkResult
import com.example.otusproject_ermoshina.domain.repositories.RepositoryDataBase
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.repositories.SuccessNetworkResult
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class SearchLoadImpTest {

    val dummyCoroutineDispatcher = Dispatchers.Unconfined

    @get: Rule
    val rule = MockKRule(this)

    @MockK
    lateinit var mockDataBase: RepositoryDataBase

    @RelaxedMockK
    lateinit var mockNetwork: RepositoryNetwork

    lateinit var testSearchLoadImp: SearchLoadImpl

    @Before
    fun init() {
        testSearchLoadImp = SearchLoadImpl(mockNetwork, mockDataBase, dummyCoroutineDispatcher)
    }


    @Test
    fun `getResultSearch CallNetworkRepository GetSuccessResult`() = runBlocking {
        val modelSearch = CreatorSearch.createModelSearch()
        val ytSearchPaging = CreatorSearch.createYTSearchPaging(modelSearch, "query")

        coEvery {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        } returns SuccessNetworkResult(modelSearch)

        val result = testSearchLoadImp.getResultSearch("query", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.getResultSearch(
                query = "query",
                maxResult = 1,
                token = "token",
                safeSearch = "strict"
            )
        }
        assertEquals(BaseViewModel.SuccessViewModel(ytSearchPaging), result)
    }

    @Test
    fun `getResultSearch CallNetworkRepository GetEmptyResult`() = runBlocking {
        val modelSearch = CreatorSearch.createModelSearch(resultSearchAllData = emptyList())

        coEvery {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        } returns SuccessNetworkResult(modelSearch)

        val result = testSearchLoadImp.getResultSearch("query", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.getResultSearch(
                query = "query",
                maxResult = 1,
                token = "token",
                safeSearch = "strict"
            )
        }
        assertEquals(BaseViewModel.EmptyResultViewModel, result)
    }

    @Test
    fun `getResultSearch CallNetworkRepository GetErrorNetworkResult`() = runBlocking {
        coEvery {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        } returns ErrorNetworkResult

        val result = testSearchLoadImp.getResultSearch("query", "token", 1)

        coVerify(exactly = 1) {
            mockNetwork.getResultSearch(
                query = "query",
                maxResult = 1,
                token = "token",
                safeSearch = "strict"
            )
        }
        assertEquals(BaseViewModel.ErrorLoadingViewModel, result)
    }

    @Test
    fun `getLoadMoreResultSearch CallNetworkRepository GetException`() = runBlocking {
        var exceptionThrown: Boolean = false
        coEvery {
            mockNetwork.getResultSearch(
                any(),
                any(),
                any(),
                any()
            )
        } throws (Exception("Network exception"))

        try {
            testSearchLoadImp.getResultSearch("query", "token", 1)
        } catch (e: Exception) {
            exceptionThrown = true
            print(e.toString())
        }

        coVerify(exactly = 1) {
            mockNetwork.getResultSearch(
                query = "query",
                maxResult = 1,
                token = "token",
                safeSearch = "strict"
            )
        }
        Assert.assertTrue(exceptionThrown)
    }

    @Test
    fun `getMainFragmentPage CallDataBase GetSuccessData`() = runBlocking {
        val queryFromDataBase =
            listOf(CreatorSearch.createRoomQueryApp(), CreatorSearch.createRoomQueryApp())
        coEvery { mockDataBase.loadQuery() } returns queryFromDataBase

        testSearchLoadImp.getMainFragmentPage(1)

        coVerify(exactly = 1) {
            mockDataBase.loadQuery()
        }
    }

    @Test
    fun `getMainFragmentPage CallNetWorkTwice VerifyCallsTwice`() = runBlocking {
        val queryFromDataBase =
            listOf(CreatorSearch.createRoomQueryApp(), CreatorSearch.createRoomQueryApp())
        val modelSearch = CreatorSearch.createModelSearch()
        coEvery { mockDataBase.loadQuery() } returns queryFromDataBase
        coEvery {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        } returns SuccessNetworkResult(modelSearch)

        testSearchLoadImp.getMainFragmentPage(1)

        coVerify(exactly = 2) {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        }
    }

    @Test
    fun `getMainFragmentPage CallNetWork VerifyCallsExactlyRightData`() = runBlocking {
        val queryFromDataBase =
            listOf(CreatorSearch.createRoomQueryApp(), CreatorSearch.createRoomQueryApp(query="queryTwo"))
        val modelSearch = CreatorSearch.createModelSearch()
        coEvery { mockDataBase.loadQuery() } returns queryFromDataBase
        coEvery {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        } returns SuccessNetworkResult(modelSearch)

        testSearchLoadImp.getMainFragmentPage(1)

        coVerify(exactly = 1) {
            mockNetwork.getResultSearch("query", 1, "","strict")
            mockNetwork.getResultSearch("queryTwo", 1, "","strict")
        }
        coVerifySequence  {
            mockNetwork.getResultSearch("query", 1, "","strict")
            mockNetwork.getResultSearch("queryTwo", 1, "","strict")
        }
    }

    @Test
    fun `getMainFragmentPage CallNetWork GetSuccessNetworkResult`() = runBlocking {
        val queryFromDataBase =
            listOf(CreatorSearch.createRoomQueryApp(), CreatorSearch.createRoomQueryApp(query="queryTwo"))
        val modelSearch = CreatorSearch.createModelSearch()
        val ytQueryOne = CreatorSearch.createYTQuery(queryFromDataBase[0])
        val ytQueryTwo = CreatorSearch.createYTQuery(queryFromDataBase[1])
        val concatResult = mutableListOf(
            CreatorSearch.createYTMainFragmentData(ytQueryOne, modelSearch),
            CreatorSearch.createYTMainFragmentData(ytQueryTwo, modelSearch))

        coEvery { mockDataBase.loadQuery() } returns queryFromDataBase
        coEvery {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        } returns SuccessNetworkResult(modelSearch)

        val result = testSearchLoadImp.getMainFragmentPage(1)

        assertEquals(BaseViewModel.SuccessViewModel(concatResult), result)
    }

    @Test
    fun `getMainFragmentPage CallNetWork GetEmptyYTMainFragmentData`() = runBlocking {
        val queryFromDataBase =
            listOf(CreatorSearch.createRoomQueryApp(), CreatorSearch.createRoomQueryApp(query="queryTwo"))
        val modelSearch = CreatorSearch.createModelSearch(resultSearchAllData = emptyList())

        coEvery { mockDataBase.loadQuery() } returns queryFromDataBase
        coEvery {
            mockNetwork.getResultSearch(any(), any(), any(), any())
        } returns SuccessNetworkResult(modelSearch)
        val result = testSearchLoadImp.getMainFragmentPage(1)

        assertEquals(BaseViewModel.ErrorLoadingViewModel, result)
    }

}