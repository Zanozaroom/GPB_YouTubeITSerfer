package com.example.otusproject_ermoshina.ui.screen.user

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.creatordata.CreatorYTPlayListData
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.di.NavigatorModule
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.espresso.HelperRecyclerView
import com.example.otusproject_ermoshina.launchFragment
import com.example.otusproject_ermoshina.rules.BaseTest
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.screen.videolist.AdapterVideoLists
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.*
import kotlinx.coroutines.flow.asFlow
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton


@UninstallModules(NavigatorModule::class, Helpers::class)
@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserPlayListFragmentTest: BaseTest() {
    @Inject
    lateinit var navigator: ContractNavigator

    @Inject
    lateinit var helper: PlayListLoad

    private lateinit var scenario: AutoCloseable
    private val resultData = listOf(CreatorYTPlayListData.listYTPlayList).asFlow()

    @After
    fun close() {
        scenario.close()
    }
    @Test
    fun launchWhenHasSuccessNetworkResult() {
        coEvery {
            helper.loadFavoritePlayList()
        } returns resultData

        scenario = launchFragment<UserPlayListFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun checkedOnRecyclerRightThenClickOnButtonOpenChannel() {
        coEvery {
            helper.loadFavoritePlayList()
        } returns resultData
        coEvery {
            navigator.startYTPlayListFragmentUserStack(any())
        } just runs

        scenario = launchFragment<UserPlayListFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<AdapterVideoLists.VideoListViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterVideoLists.VideoListViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.titleChannel)
                )
            )

        verify {
            navigator.startYTPlayListFragmentUserStack("idChannel3")
        }
    }
    @Test
    fun checkedOnRecyclerRightThenClickOnButtonOpenVideoList() {
        coEvery {
            helper.loadFavoritePlayList()
        } returns resultData
        coEvery {
            navigator.startYTVideoListFragmentUserStack(any())
        } just runs

        scenario = launchFragment<UserPlayListFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<AdapterVideoLists.VideoListViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterVideoLists.VideoListViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.btnOpenThisPL)
                )
            )

        verify {
            navigator.startYTVideoListFragmentUserStack("idList3")
        }
    }

    @Test
    fun checkedOnRecyclerRightThenClickOnImageThenOpenVideoList() {
        coEvery {
            helper.loadFavoritePlayList()
        } returns resultData
        coEvery {
            navigator.startYTVideoListFragmentUserStack(any())
        } just runs

        scenario = launchFragment<UserPlayListFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<AdapterVideoLists.VideoListViewHolder>(3))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterVideoLists.VideoListViewHolder>(
                    3,
                    HelperRecyclerView.clickChildViewWithId(R.id.imageVideo)
                )
            )
        verify {
            navigator.startYTVideoListFragmentUserStack("idList4")
        }
    }

    @Test
    fun checkedOnRecyclerRightThenClickDeleteFromFavorite() {
        coEvery {
            helper.loadFavoritePlayList()
        } returns resultData

        scenario = launchFragment<UserPlayListFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<AdapterVideoLists.VideoListViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterVideoLists.VideoListViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.btnNegativeActionPlaylist)
                )
            )
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