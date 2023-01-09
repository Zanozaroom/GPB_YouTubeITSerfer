package com.example.otusproject_ermoshina.ui.screen.video

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.creatordata.CreatorYTVideo
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.di.NavigatorModule
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.launchFragment
import com.example.otusproject_ermoshina.rules.BaseTest
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
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
class PageOfVideoFragmentTest: BaseTest() {
    @Inject
    lateinit var navigator: ContractNavigator

    @Inject
    lateinit var helper: VideoLoad

    private lateinit var scenario: AutoCloseable
    private val resultData = CreatorYTVideo.createYTVideo()

    @After
    fun close() {
        scenario.close()
    }
    @Test
    fun launchWhenHasErrorNetworkResult(){
        coEvery {
            helper.loadingYTVideo(any())
        } returns BaseViewModel.ErrorLoadingViewModel

        scenario = launchFragment {
            PageOfVideoFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.buttonErrorLoad)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.messageErrorLoad)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
    @Test
    fun launchWhenHasLoadingNetworkResult(){
        coEvery {
            helper.loadingYTVideo(any())
        } returns BaseViewModel.LoadingViewModel

        scenario = launchFragment {
            PageOfVideoFragment.newInstance("idVideo")
        }

        Espresso.onView(ViewMatchers.withId(R.id.progressBar)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
    @Test
    fun launchWhenHasSuccessNetworkResult(){
        coEvery {
            helper.loadingYTVideo(any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        scenario = launchFragment {
            PageOfVideoFragment.newInstance("idVideo")
        }
        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.addVideoToFavorite)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.titleChannel)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.titleVideo)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.descriptionVideo)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.iconAllWatched)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.iconLiked)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.textAllWatched)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        Espresso.onView(ViewMatchers.withId(R.id.textAllLiked)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }
    @Test
    fun checkedClickOnChannel() {
        coEvery {
            helper.loadingYTVideo(any())
        } returns BaseViewModel.SuccessViewModel(resultData)
        coEvery {
            navigator.startYTPlayListFragmentMainStack(any())
        } just runs

        scenario = launchFragment {
            PageOfVideoFragment.newInstance("idVideo")
        }
        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.titleChannel))
            .perform(click())

        verify {
            navigator.startYTPlayListFragmentMainStack("channelId")
        }
    }
    @Test
    fun checkedClickOnAddVideo() {
        coEvery {
            helper.loadingYTVideo(any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        scenario = launchFragment {
            PageOfVideoFragment.newInstance("idVideo")
        }
        Thread.sleep(5000)

        Espresso.onView(ViewMatchers.withId(R.id.addVideoToFavorite))
            .perform(click())
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