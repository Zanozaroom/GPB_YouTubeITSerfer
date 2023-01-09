package com.example.otusproject_ermoshina.ui.screen.video

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.rules.ViewModelRules
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class PageOfVideoVMTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val ruleViewModelRules = ViewModelRules()

    @get:Rule
    val ruleInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var helper: VideoLoad

    lateinit var pageOfVideoVM: PageOfVideoVM

    @Before
    fun init() {
        val savedState = SavedStateHandle(mapOf("idVideo" to "idVideo"))
        pageOfVideoVM = spyk(PageOfVideoVM(savedState, helper))
    }

    @Test
    fun `catchException GetNetworkLoadException`() {
        val exception = NetworkLoadException("NetworkLoadException")

        coEvery { helper.loadingYTVideo(any()) } throws exception
        every { pageOfVideoVM.catchException(any()) } answers {
            print(args[0].toString())
        }

        pageOfVideoVM.tryLoad()

        assertTrue(pageOfVideoVM.screenState.value is BaseViewModel.ErrorLoadingViewModel)
        coVerify { helper.loadingYTVideo("idVideo") }
        coVerify { pageOfVideoVM.catchException(any()) }
    }

    @Test
    fun `firstLoadPlayList GetSuccessState`() {
        val video = mockk<YTVideo>()

        coEvery { helper.loadingYTVideo(any()) } returns BaseViewModel.SuccessViewModel(video)

        pageOfVideoVM.tryLoad()

        assertTrue(pageOfVideoVM.screenState.value is BaseViewModel.SuccessViewModel)
        coVerify { helper.loadingYTVideo("idVideo") }
    }

    @Test
    fun `addVideoToFavorite RunsWithoutExceptionAndShowToast`() {
        val video = mockk<YTVideo>()

        coEvery { helper.saveVideo(any()) } just runs

        pageOfVideoVM.addVideoToFavorite(video)

        verify { pageOfVideoVM.showToast(R.string.toastAddFavoriteVideo) }
    }

    @Test
    fun `addVideoToFavorite RunsWithExceptionAndShowToast`() {
        val video = mockk<YTVideo>()

        coEvery { helper.saveVideo(any()) } throws DataBaseLoadException("")

        pageOfVideoVM.addVideoToFavorite(video)

        verify { pageOfVideoVM.showToast(R.string.toastAddFavoriteFail) }
    }
}