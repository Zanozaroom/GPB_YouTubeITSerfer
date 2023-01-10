package com.example.otusproject_ermoshina.ui.screen.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.di.Helpers
import com.example.otusproject_ermoshina.di.NavigatorModule
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.espresso.HelperRecyclerView
import com.example.otusproject_ermoshina.creatordata.CreatorMainFragmentData.createResultForRecyclerMainFragment
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
class FragmentMainTest: BaseTest() {

    @Inject
    lateinit var navigator: ContractNavigator

    @Inject
    lateinit var helper: SearchLoad

    private lateinit var scenario: AutoCloseable

    private val resultData = createResultForRecyclerMainFragment()

    @After
    fun close(){
        scenario.close()
    }

    @Test
    fun launchWhenHasErrorNetworkResult() {
        coEvery {
            helper.getMainFragmentPage(any())
        } returns BaseViewModel.ErrorLoadingViewModel
        scenario = launchFragment<FragmentMain>()

        onView(withId(R.id.messageErrorLoad)).check(
            matches(isDisplayed()))
        onView(withId(R.id.buttonErrorLoad)).check(
            matches(isDisplayed()))
    }

    @Test
    fun launchWhenHasLoadingResult() {
        coEvery {helper.getMainFragmentPage(any())
        } returns BaseViewModel.LoadingViewModel
        scenario = launchFragment<FragmentMain>()

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }

    @Test
    fun launchWhenHasSuccessNetworkResult() {
        coEvery {
            helper.getMainFragmentPage(any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        scenario = launchFragment<FragmentMain>()
        onView(withId(R.id.recyclerVideoList)).check(matches(isDisplayed()))
    }

    @Test
    fun checkedThatRecyclerHasRightChildCount() {
        coEvery {
            helper.getMainFragmentPage(any())
        } returns BaseViewModel.SuccessViewModel(resultData)

        scenario = launchFragment<FragmentMain>()

        onView(withId(R.id.recyclerVideoList))
            .perform(scrollToPosition<AdapterMainParent.ViewHolderMain>(0))
            .check(matches(hasChildCount(3)))
    }

    @Test
    fun checkedOnRecyclerRightThenClickOnButtonOpenAllResultSearch() {
        coEvery {
            helper.getMainFragmentPage(any())
        } returns BaseViewModel.SuccessViewModel(resultData)
        coEvery {
            navigator.startSearchFragmentMainStack(any(), any())
        } just runs

        scenario = launchFragment<FragmentMain>()

        onView(withId(R.id.recyclerVideoList))
            .perform(scrollToPosition<AdapterMainParent.ViewHolderMain>(2))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<AdapterMainParent.ViewHolderMain>
                    (2, HelperRecyclerView.clickChildViewWithId(R.id.openMore)))

        verify {
            navigator.startSearchFragmentMainStack("query3","title")
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




