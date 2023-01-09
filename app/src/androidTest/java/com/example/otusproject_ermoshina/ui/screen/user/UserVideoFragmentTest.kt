package com.example.otusproject_ermoshina.ui.screen.user

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.creatordata.CreatorYTVideo
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.di.NavigatorModule
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.espresso.HelperRecyclerView
import com.example.otusproject_ermoshina.launchFragment
import com.example.otusproject_ermoshina.rules.BaseTest
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.screen.user.UserVideoAdapter.VideoUserViewHolder
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
class UserVideoFragmentTest: BaseTest() {
    @Inject
    lateinit var navigator: ContractNavigator

    @Inject
    lateinit var helper: VideoLoad

    private lateinit var scenario: AutoCloseable
    private val listOfVideo = listOf(
        CreatorYTVideo.createYTVideo(idVideo = "idVideo1"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo2"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo3"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo4"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo5"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo6")
    )
    private val resultData = listOf(listOfVideo).asFlow()

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun launchWhenHasSuccessNetworkResult() {
        coEvery {
            helper.loadFavoriteVideo()
        } returns resultData

        scenario = launchFragment<UserVideoFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
    @Test
    fun checkedOnRecyclerRightThenClickOnButtonOpenChannel() {
        coEvery {
            helper.loadFavoriteVideo()
        } returns resultData
        coEvery {
            navigator.startYTPlayListFragmentUserStack(any())
        } just runs

        scenario = launchFragment<UserVideoFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoUserViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoUserViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.titleChannel)
                )
            )

        verify {
            navigator.startYTPlayListFragmentUserStack("channelId")
        }
    }

    @Test
    fun checkedOnRecyclerRightThenClickOnImageThenOpenVideo() {
        coEvery {
            helper.loadFavoriteVideo()
        } returns resultData
        coEvery {
            navigator.startPageOfVideoFragmentUserStack(any())
        } just runs

        scenario = launchFragment<UserVideoFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoUserViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoUserViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.imageVideo)
                )
            )

        verify {
            navigator.startPageOfVideoFragmentUserStack("idVideo3")
        }
    }
    @Test
    fun checkedOnRecyclerRightThenClickOnButtonThenOpenVideo() {
        coEvery {
            helper.loadFavoriteVideo()
        } returns resultData
        coEvery {
            navigator.startPageOfVideoFragmentUserStack(any())
        } just runs

        scenario = launchFragment<UserVideoFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoUserViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoUserViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.buttonShowVideo)
                )
            )

        verify {
            navigator.startPageOfVideoFragmentUserStack("idVideo3")
        }
    }

    @Test
    fun checkedOnRecyclerRightThenClickDeleteVideo() {
        coEvery {
            helper.loadFavoriteVideo()
        } returns resultData

        scenario = launchFragment<UserVideoFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<VideoUserViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<VideoUserViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.actionNegativeButton)))
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