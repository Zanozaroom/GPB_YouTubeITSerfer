package com.example.otusproject_ermoshina.ui.screen.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.otusproject_ermoshina.MainActivity
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.creatordata.*
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.domain.helpers.VideoListLoad
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.espresso.HelperRecyclerView
import com.example.otusproject_ermoshina.rules.BaseTest
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.screen.search.AdapterSearch
import com.example.otusproject_ermoshina.ui.screen.user.UserVideoAdapter
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.flow.asFlow
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@UninstallModules(Helpers::class)
@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FragmentMainTestScenario : BaseTest() {
    @Inject
    lateinit var helperMainFragment: SearchLoad
    @Inject
    lateinit var helperSearchFragment: SearchLoad
    @Inject
    lateinit var helperPlayListFragment: PlayListLoad
    @Inject
    lateinit var helperVideoList: VideoListLoad
    @Inject
    lateinit var helperVideo: VideoLoad
    private lateinit var scenario: ActivityScenario<MainActivity>

    private val resultDataMainFragment =
        CreatorMainFragmentData.createResultForRecyclerMainFragment()
    private val resultDataSearchFragment = CreatorYTSearchPaging.createYTSearchPaging()
    private val resultDataPageOfVideo = CreatorYTVideo.createYTVideo()
    private val resultDataChannelPlayList = CreatorYTPlayListData.createYTPlayListPaging()

    private val listOfUserVideo = mutableListOf<YTVideo>()
    private val resultDataUserVideo = listOf(listOfUserVideo).asFlow()


    @Before
    override fun setUp() {
        super.setUp()
        coEvery {
            helperMainFragment.getMainFragmentPage(any())
        } returns BaseViewModel.SuccessViewModel(resultDataMainFragment)
        coEvery {
            helperSearchFragment.getResultSearch(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultDataSearchFragment)
        coEvery {
            helperVideo.loadingYTVideo(any())
        } returns BaseViewModel.SuccessViewModel(resultDataPageOfVideo)
        every {
            helperVideo.loadFavoriteVideo()
        } returns resultDataUserVideo
        coEvery {
            helperPlayListFragment.firstLoadPlayList(any(), any(), any())
        } returns BaseViewModel.SuccessViewModel(resultDataChannelPlayList)
        Thread.sleep(5000)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun launchWhenHasSuccessNetworkResult() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        checkMainPageIsDisplayed()
        clickOnButtonOpenAllResultSearch()
        clickOnSearchFragmentToOpenVideo()
        clickAddVideoToFavorite()
        openUserMenu()
        swipeToPageFavoriteVideoAndCheckThatHasCorrectVideo()
        clickOnFragmentUserVideoOpenChannel()
    }


    private fun checkMainPageIsDisplayed() {
        Espresso.onView(withId(R.id.recyclerVideoList))
            .check(matches(isDisplayed()))
    }

    private fun clickOnButtonOpenAllResultSearch() {
        Espresso.onView(withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<AdapterMainParent.ViewHolderMain>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterMainParent.ViewHolderMain>
                    (2, HelperRecyclerView.clickChildViewWithId(R.id.openMore))
            )
    }

    private fun clickOnSearchFragmentToOpenVideo() {
        Espresso.onView(withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<AdapterSearch.VideoViewHolderSearch>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterSearch.VideoViewHolderSearch>(
                    2,
                    HelperRecyclerView.clickChildViewWithId(R.id.imageVideo)
                )
            )
    }

    private fun clickAddVideoToFavorite() {
        Espresso.onView(withId(R.id.addVideoToFavorite))
            .perform(click())
        listOfUserVideo.add(resultDataPageOfVideo)
    }

    private fun openUserMenu() {
        Espresso.onView(withId(R.id.bottom_nav))
            .check(matches(isDisplayed()))
        Espresso.onView(
            allOf(
                withId(R.id.myvideos),
                isDescendantOfA(withId(R.id.bottom_nav))
            )
        ).check(matches(isDisplayed()))
            .perform(click())
    }

    private fun swipeToPageFavoriteVideoAndCheckThatHasCorrectVideo() {
        Espresso.onView(withId(R.id.viewPager))
            .perform(ViewActions.swipeLeft())
        Espresso.onView(withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<UserVideoAdapter.VideoUserViewHolder>(0))
            .check(
                (matches(
                    HelperRecyclerView.atPosition(
                        0, hasDescendant(
                            allOf(
                                withId(R.id.titleChannel),
                                withText("channelTitle")
                            )
                        )
                    )
                )
                        )
            )
    }

    private fun clickOnFragmentUserVideoOpenChannel(){
        Espresso.onView(withId(R.id.recyclerVideoList))
            .perform(RecyclerViewActions.scrollToPosition<UserVideoAdapter.VideoUserViewHolder>(0))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<UserVideoAdapter.VideoUserViewHolder>(
                    0,
                    HelperRecyclerView.clickChildViewWithId(R.id.titleChannel)
                )
            )
    }

}