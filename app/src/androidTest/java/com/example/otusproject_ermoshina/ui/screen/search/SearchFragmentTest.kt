package com.example.otusproject_ermoshina.ui.screen.search

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.creatordata.CreatorYTSearchPaging
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.di.NavigatorModule
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.espresso.HelperRecyclerView
import com.example.otusproject_ermoshina.launchFragment
import com.example.otusproject_ermoshina.rules.BaseTest
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.screen.search.AdapterSearch.VideoViewHolderSearch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.*
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton

@UninstallModules(NavigatorModule::class, Helpers::class)
@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest: BaseTest() {
    @Inject
    lateinit var navigator: ContractNavigator

    @Inject
    lateinit var helper: SearchLoad

    private lateinit var scenario: AutoCloseable
    private val resultData = CreatorYTSearchPaging.createYTSearchPaging()

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun launchWhenHasErrorNetworkResult(){
        coEvery {
            helper.getResultSearch(any(), any(), any())
        } returns BaseViewModel.ErrorLoadingViewModel

        scenario = launchFragment {
            SearchFragment.newInstance("query")
        }

        Espresso.onView(ViewMatchers.withId(R.id.buttonErrorLoad)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.messageErrorLoad)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun launchWhenHasLoadingResult() {
        coEvery {
            helper.getResultSearch(any(), any(), any())
        } returns BaseViewModel.LoadingViewModel

        scenario = launchFragment {
            SearchFragment.newInstance("query")
        }

        Espresso.onView(ViewMatchers.withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun launchWhenHasSuccessResult() {
        coEvery {
            helper.getResultSearch(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        scenario = launchFragment {
            SearchFragment.newInstance("query")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun checkedOnRecyclerRightThenClickOnOpenChannel() {
        coEvery {
            helper.getResultSearch(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)
        coEvery {
            navigator.startYTPlayListFragmentMainStack(any())
        } just runs

        scenario = launchFragment {
            SearchFragment.newInstance("query")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoViewHolderSearch>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoViewHolderSearch>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.btnOpenChannel)
                )
            )

        verify {
            navigator.startYTPlayListFragmentMainStack("channelId3")
        }
    }
    @Test
    fun checkedOnRecyclerRightThenClickOnOpenVideo() {
        coEvery {
            helper.getResultSearch(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultData)
        coEvery {
            navigator.startPageOfVideoFragmentMainStack(any())
        } just runs

        scenario = launchFragment {
            SearchFragment.newInstance("query")
        }

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoViewHolderSearch>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoViewHolderSearch>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.imageVideo)
                )
            )
        verify {
            navigator.startPageOfVideoFragmentMainStack("videoId3")
        }
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class Faker {
        @Provides
        @Singleton
        fun provideNavigator(): ContractNavigator {
            return mockk(relaxed = true)
        }
    }
}