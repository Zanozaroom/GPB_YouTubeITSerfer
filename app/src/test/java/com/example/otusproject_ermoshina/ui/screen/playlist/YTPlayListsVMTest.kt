package com.example.otusproject_ermoshina.ui.screen.playlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.helpers.ViewModelRules
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class YTPlayListsVMTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val ruleViewModelRules = ViewModelRules()

    @get:Rule
    val ruleInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var helper: PlayListLoad

    lateinit var ytPlayListsVM: YTPlayListsVM


    @Before
    fun init() {
        val savedState = SavedStateHandle(mapOf("idChannel" to "idChannel"))
        ytPlayListsVM = spyk(YTPlayListsVM(savedState, helper))
    }


    @Test
    fun `catchException GetNetworkLoadException`() {
        val exception = NetworkLoadException("NetworkLoadException")

        coEvery { helper.firstLoadPlayList(any(), any(), any()) } throws exception
        every { ytPlayListsVM.catchException(any())} answers {
            print (args[0].toString())
        }

        ytPlayListsVM.firstLoadPlayList("")

        assertTrue(ytPlayListsVM.screenState.value is BaseViewModel.ErrorLoadingViewModel)
        coVerify(exactly = 1) { helper.firstLoadPlayList("", "", 6) }
        coVerify { ytPlayListsVM.catchException(any()) }

    }

    @Test
    fun `firstLoadPlayList GetSuccessState`() {
        val ytPlayListPaging = mockk<YTPlayListPaging>()

        coEvery {
            helper.firstLoadPlayList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(ytPlayListPaging)

        ytPlayListsVM.firstLoadPlayList("")

        assertTrue(ytPlayListsVM.screenState.value is BaseViewModel.SuccessViewModel)
        coVerify { helper.firstLoadPlayList("", "", 6) }
        coVerify { ytPlayListsVM.firstLoadPlayList("") }
    }


    @Test
    fun `loadMore GetEmptyTokenForLoading`(){
        val ytPlayListPaging = mockk<YTPlayListPaging>()
        every { ytPlayListPaging.nextToken } returns ""

        ytPlayListsVM.loadMore(ytPlayListPaging)

        assertTrue(ytPlayListsVM.screenState.value is BaseViewModel.NotMoreLoadingViewModel)
        coVerify(exactly = 0) { helper.loadMorePlayList(ytPlayListPaging, "", 6) }
    }

    @Test
    fun `loadMore GetTokenForLoading GetSuccessResultFromNetwork`(){
        val ytPlayListPaging = mockk<YTPlayListPaging>()
        every { ytPlayListPaging.nextToken } returns "nextToken"
        coEvery {
            helper.loadMorePlayList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(ytPlayListPaging)

        ytPlayListsVM.loadMore(ytPlayListPaging)

        assertTrue(ytPlayListsVM.screenState.value is BaseViewModel.SuccessViewModel)
        coVerify(exactly = 1) { helper.loadMorePlayList(ytPlayListPaging, "nextToken", 4) }
    }

    @Test
    fun `loadMore GetTokenForLoading GetErrorResult`() {
        val ytPlayListPaging = mockk<YTPlayListPaging>()
        every { ytPlayListPaging.nextToken } returns "nextToken"
        coEvery {
            helper.loadMorePlayList(any(), any(), any())
        } returns BaseViewModel.ErrorLoadingViewModel

        ytPlayListsVM.loadMore(ytPlayListPaging)

        assertTrue(ytPlayListsVM.screenState.value is BaseViewModel.ErrorLoadingViewModel)
        coVerify(exactly = 1) { helper.loadMorePlayList(ytPlayListPaging, "nextToken", 4) }
    }

    @Test
    fun `loadMore GetTokenForLoading GetNetWorkException`() {
        val ytPlayListPaging = mockk<YTPlayListPaging>()

        every { ytPlayListPaging.nextToken } returns "nextToken"
        coEvery {
            helper.loadMorePlayList(any(), any(), any())
        } throws NetworkLoadException("NetworkLoadException")
        every { ytPlayListsVM.catchException(any())} answers {
            print (args[0].toString())
        }

        ytPlayListsVM.loadMore(ytPlayListPaging)

        coVerify(exactly = 1) { helper.loadMorePlayList(ytPlayListPaging, "nextToken", 4) }
        coVerify(exactly = 1) { ytPlayListsVM.catchException(any()) }
    }

    @Test
    fun `addToFavoritePL RunsWithoutExceptionAndShowToast`(){
        val list = mockk<YTPlayList>()
        var isRunSuccess = true
        coEvery { helper.addToFavoritePlayList(any())} just runs

        ytPlayListsVM.addToFavoritePL(list)

        verify { ytPlayListsVM.showToast(R.string.toastAddFavoritePlayList) }
    }


    @Test
    fun `addToFavoritePL RunsWithException`() = runBlocking{
        val list = mockk<YTPlayList>()
        var isRunFalse = false
        coEvery { helper.addToFavoritePlayList(any())} throws DataBaseLoadException("")

        try{
            helper.addToFavoritePlayList(list)
        }catch (e:Exception){
            isRunFalse = true
        }
        assertTrue(isRunFalse)
    }

    @Test
    fun `addToFavoritePL RunsWithException CatchDataBaseLoadExceptionAndShowToast`(){
        val list = mockk<YTPlayList>()
        coEvery { helper.addToFavoritePlayList(any())} throws DataBaseLoadException("")

        ytPlayListsVM.addToFavoritePL(list)

       verify { ytPlayListsVM.showToast(R.string.messageErrorDataBaseSave) }

    }
}