package com.example.otusproject_ermoshina.ui.screen.videolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.helpers.VideoListLoad
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.rules.ViewModelRules
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class YTVideoListVMTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val ruleViewModelRules = ViewModelRules()

    @get:Rule
    val ruleInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var helper: VideoListLoad

    lateinit var ytVideoListVM: YTVideoListVM

    @Before
    fun init() {
        val savedState = SavedStateHandle(mapOf("idPlayList" to "idPlayList"))
        ytVideoListVM = spyk(YTVideoListVM(savedState, helper))
    }


    @Test
    fun `catchException GetNetworkLoadException`() {
        val exception = NetworkLoadException("NetworkLoadException")

        coEvery { helper.getVideoList(any(),any(),any()) } throws exception
        every { ytVideoListVM.catchException(any())} answers {
            print (args[0].toString())
        }

            ytVideoListVM.tryLoad()

        assertTrue(ytVideoListVM.screenState.value is BaseViewModel.ErrorLoadingViewModel)
        coVerify { ytVideoListVM.catchException(any()) }
    }

    @Test
    fun `firstLoadPlayList GetSuccessState`() {
        val videoListPaging = mockk<YTVideoListPaging>()

        coEvery { helper.getVideoList(any(),any(),any()) } returns BaseViewModel.SuccessViewModel(videoListPaging)

        ytVideoListVM.tryLoad()

        coVerify {helper.getVideoList("idPlayList","",6) }
        assertTrue(ytVideoListVM.screenState.value is BaseViewModel.SuccessViewModel)

    }
    @Test
    fun `firstLoadPlayList EmptyResultViewModel`() {
        coEvery { helper.getVideoList(any(),any(),any()) } returns BaseViewModel.EmptyResultViewModel

        ytVideoListVM.tryLoad()

        coVerify {helper.getVideoList("idPlayList","",6) }
        assertTrue(ytVideoListVM.screenState.value is BaseViewModel.EmptyResultViewModel)
    }

    @Test
    fun `loadMore GetEmptyTokenForLoading`() {
        val ytVideoListPaging = mockk<YTVideoListPaging>()
        every {ytVideoListPaging.nextToken} returns ""

        ytVideoListVM.loadMore(ytVideoListPaging)

        verify { ytVideoListVM.showToast(R.string.toastAllVideoLoad) }

    }

    @Test
    fun `loadMore GetErrorState`() {
        val ytVideoListPaging = mockk<YTVideoListPaging>()

        every {ytVideoListPaging.nextToken} returns "nextToken"
        coEvery { helper.getLoadMoreVideoList(any(),any()) } returns BaseViewModel.ErrorLoadingViewModel

        ytVideoListVM.loadMore(ytVideoListPaging)

        verify { ytVideoListVM.showToast(R.string.messageNetworkLoadException) }
    }

    @Test
    fun `loadMore GetResult`() {
        val ytVideoListPaging = mockk<YTVideoListPaging>()

        every {ytVideoListPaging.nextToken} returns "nextToken"
        coEvery { helper.getLoadMoreVideoList(any(),any()) } returns BaseViewModel.SuccessViewModel(ytVideoListPaging)

        ytVideoListVM.loadMore(ytVideoListPaging)

        assertTrue(ytVideoListVM.screenState.value is BaseViewModel.SuccessViewModel)
    }

    @Test
    fun `loadMore GetException`() {
        val ytVideoListPaging = mockk<YTVideoListPaging>()

        every {ytVideoListPaging.nextToken} returns "nextToken"
        coEvery { helper.getLoadMoreVideoList(any(),any()) } throws NetworkLoadException("")
        every { ytVideoListVM.catchException(any())} answers {
            print (args[0].toString())
        }

        ytVideoListVM.loadMore(ytVideoListPaging)

        coVerify { ytVideoListVM.catchException(any()) }
    }

    @Test
    fun `addVideoToFavorite GetExceptionFromNetwork`() {
        coEvery { helper.loadVideoFromYouTubeForSave(any()) } throws NetworkLoadException("")
        every { ytVideoListVM.catchException(any())} answers {
            print (args[0].toString())
        }

        ytVideoListVM.addVideoToFavorite("idVideo")

        coVerify { ytVideoListVM.catchException(any()) }
        coVerify { ytVideoListVM.showToast(R.string.toastAddFavoriteFail) }
    }

    @Test
    fun `addVideoToFavorite GetExceptionFromDataBase`() {
        coEvery { helper.saveVideo(any()) } throws NetworkLoadException("")
        every { ytVideoListVM.catchException(any())} answers {
            print (args[0].toString())
        }

        ytVideoListVM.addVideoToFavorite("idVideo")

        coVerify { ytVideoListVM.catchException(any()) }
        coVerify { ytVideoListVM.showToast(R.string.toastAddFavoriteFail) }
    }

    @Test
    fun `addVideoToFavorite GetEmptyDataFromNetwork`() {
        val video:YTVideo? = null
        coEvery { helper.loadVideoFromYouTubeForSave(any()) } returns video

        ytVideoListVM.addVideoToFavorite("idVideo")

        coVerify { ytVideoListVM.showToast(R.string.toastAddFavoriteFail) }
    }

    @Test
    fun `addVideoToFavorite GetSuccessResult`() {
        val video = mockk<YTVideo>(relaxed = true)
        coEvery { helper.loadVideoFromYouTubeForSave(any()) } returns video

        ytVideoListVM.addVideoToFavorite("idVideo")

        coVerify { ytVideoListVM.showToast(R.string.toastAddFavoriteVideo) }
    }
}

