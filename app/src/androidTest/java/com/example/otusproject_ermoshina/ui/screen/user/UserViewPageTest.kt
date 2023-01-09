package com.example.otusproject_ermoshina.ui.screen.user

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.creatordata.CreatorYTPlayListData
import com.example.otusproject_ermoshina.creatordata.CreatorYTVideo
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.di.NavigatorModule
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
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
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.asFlow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Singleton

@UninstallModules(NavigatorModule::class, Helpers::class)
@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserViewPageTest: BaseTest() {
    @Inject
    lateinit var navigator: ContractNavigator

    @Inject
    lateinit var helperPlayList: PlayListLoad
    @Inject
    lateinit var helperVideoList: VideoLoad

    private lateinit var scenario: AutoCloseable

    private val listOfVideo = listOf(
        CreatorYTVideo.createYTVideo(idVideo = "idVideo1"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo2"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo3"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo4"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo5"),
        CreatorYTVideo.createYTVideo(idVideo = "idVideo6")
    )
    private val resultDataUserVideoFragment = listOf(listOfVideo).asFlow()
    private val resultUserPlayListFragment = listOf(CreatorYTPlayListData.listYTPlayList).asFlow()

    @Before
    override fun setUp(){
        super.setUp()
        coEvery {
            helperVideoList.loadFavoriteVideo()
        } returns resultDataUserVideoFragment
        coEvery {
            helperPlayList.loadFavoritePlayList()
        } returns resultUserPlayListFragment

        scenario = launchFragment<UserViewPage>()
    }

    @After
    fun close() {
        scenario.close()
    }
    @Test
    fun launchWhenHasSuccessNetworkResult() {
        Espresso.onView(ViewMatchers.withId(R.id.viewPager)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
    @Test
    fun swipeToPage() {
        Espresso.onView(ViewMatchers.withId(R.id.viewPager))
            .perform(swipeLeft())
            .perform(swipeRight())
    }

    @Test
    fun clickOnRecyclerView() {
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
    fun swipeToPageThenClickOnRecyclerView() {
        Espresso.onView(ViewMatchers.withId(R.id.viewPager))
            .perform(swipeLeft())

        Espresso.onView(ViewMatchers.withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<UserVideoAdapter.VideoUserViewHolder>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<UserVideoAdapter.VideoUserViewHolder>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.titleChannel)
                )
            )

        verify {
            navigator.startYTPlayListFragmentUserStack("channelId")
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