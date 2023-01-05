package com.example.otusproject_ermoshina.ui.screen.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.helpers.ViewModelRules
import com.example.otusproject_ermoshina.ui.screen.playlist.YTPlayListsVM
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class UserPlayListViewModelTest {

  @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val ruleViewModelRules = ViewModelRules()

    @get:Rule
    val ruleInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var helper: PlayListLoad

    lateinit var userPlayListViewModel: UserPlayListViewModel

    @Before
    fun init() {
        userPlayListViewModel = spyk(UserPlayListViewModel(helper))
    }

    @Test
    fun `deletePlayListFromFavorite GetDataBaseException`() = runBlocking {
        val exception = DataBaseLoadException("DataBaseLoadException")

        coEvery { helper.deletePlayListFromFavorite(any()) } throws exception

        userPlayListViewModel.deletePlayList("IdPlayList")

        coVerify(exactly = 1) { helper.deletePlayListFromFavorite("IdPlayList") }
        coVerify { userPlayListViewModel.showToast(R.string.toastRemoveFromFavoriteFail) }
    }


    @Test
    fun `deletePlayListFromFavorite GetSuccessResult`() = runBlocking {
        coEvery { helper.deletePlayListFromFavorite(any()) } just runs

        userPlayListViewModel.deletePlayList("IdPlayList")

        coVerify(exactly = 1) { helper.deletePlayListFromFavorite("IdPlayList") }
        coVerify { userPlayListViewModel.showToast(R.string.toastRemoveFromFavorite) }
    }
}