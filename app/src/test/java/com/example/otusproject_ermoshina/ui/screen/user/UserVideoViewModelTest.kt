package com.example.otusproject_ermoshina.ui.screen.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.helpers.ViewModelRules
import com.example.otusproject_ermoshina.ui.screen.playlist.YTPlayListsVM
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class UserVideoViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val ruleViewModelRules = ViewModelRules()

    @get:Rule
    val ruleInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var helper: VideoLoad

    lateinit var userVideoViewModel: UserVideoViewModel

    @Before
    fun init() {
        userVideoViewModel = spyk(UserVideoViewModel(helper))
    }

    @Test
    fun `catchException GetDataBaseException`() = runBlocking {
        val exception = DataBaseLoadException("DataBaseLoadException")
        val video = mockk<YTVideo>()

        coEvery { helper.deleteVideo(any()) } throws exception

        userVideoViewModel.deleteVideo(video)

        coVerify(exactly = 1) { helper.deleteVideo(video) }
        coVerify { userVideoViewModel.showToast(R.string.toastRemoveFromFavoriteFail) }
    }

    @Test
    fun `deleteVideo GetSuccessResult`() = runBlocking {
        val video = mockk<YTVideo>()

        coEvery { helper.deleteVideo(any()) } just runs

        userVideoViewModel.deleteVideo(video)

        coVerify(exactly = 1) { helper.deleteVideo(video) }
        coVerify { userVideoViewModel.showToast(R.string.toastRemoveFromFavorite) }
    }
}