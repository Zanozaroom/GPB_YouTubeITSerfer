package com.example.otusproject_ermoshina.ui.screen.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.domain.model.YTSearchPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.rules.ViewModelRules
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SearchFragmentVMTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val ruleViewModelRules = ViewModelRules()

    @get:Rule
    val ruleInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var helper: SearchLoad

    lateinit var searchFragmentVM: SearchFragmentVM

    @RelaxedMockK
    lateinit var viewModel: BaseViewModel


    @Before
    fun init() {
        val savedState = SavedStateHandle(mapOf("querty" to "querty"))
        searchFragmentVM = spyk(SearchFragmentVM(helper, savedState))
    }

    @Test
    fun `catchException GetNetworkLoadException`() = runBlocking {
        val exception = NetworkLoadException("NetworkLoadException")

        coEvery { helper.getResultSearch(any(), any(), any()) } throws exception
        every { searchFragmentVM.catchException(any()) } answers {
            print(args[0].toString())
        }

        searchFragmentVM.load()

        assertTrue(searchFragmentVM.state.value is BaseViewModel.ErrorLoadingViewModel)
        verify { searchFragmentVM.catchException(any()) }
    }

    @Test
    fun tryCallsHelperGetResultSearch() = runBlocking {
        val ytSearchPaging = mockk<YTSearchPaging>()

        coEvery {
            helper.getResultSearch("querty", any(), any())
        } returns BaseViewModel.SuccessViewModel(ytSearchPaging)

        searchFragmentVM.load()

        assertTrue(searchFragmentVM.state.value is BaseViewModel.SuccessViewModel)
        coVerify { helper.getResultSearch("querty", any(), any()) }
    }

    @Test
    fun `loadMore catchException GetNetworkLoadException`() = runBlocking {
        val exception = NetworkLoadException("NetworkLoadException")
        val ytSearchPaging = mockk<YTSearchPaging>()

        coEvery { helper.getLoadMoreResultSearch(any(), any()) } throws exception
        every { searchFragmentVM.catchException(any()) } answers {
            print(args[0].toString())
        }
        every { ytSearchPaging.nextToken } returns "nextToken"

        searchFragmentVM.loadMore(ytSearchPaging)

        assertTrue(searchFragmentVM.state.value is BaseViewModel.ErrorLoadingViewModel)
        verify { searchFragmentVM.catchException(any()) }
    }

    @Test
    fun `loadMore getEmptyToken ShowToast`() = runBlocking {
        val ytSearchPaging = mockk<YTSearchPaging>()

        every { ytSearchPaging.nextToken } returns ""

        searchFragmentVM.loadMore(ytSearchPaging)

        verify { searchFragmentVM.showToast(R.string.toastAllVideoLoad) }
    }

    @Test
    fun `loadMore getSuccessResult`() = runBlocking {
        val ytSearchPaging = mockk<YTSearchPaging>()

        coEvery { helper.getLoadMoreResultSearch(any(), any())
        } returns  BaseViewModel.SuccessViewModel(ytSearchPaging)

        every { ytSearchPaging.nextToken } returns "nextToken"

        searchFragmentVM.loadMore(ytSearchPaging)

        assertTrue(searchFragmentVM.state.value is BaseViewModel.SuccessViewModel)
    }

}