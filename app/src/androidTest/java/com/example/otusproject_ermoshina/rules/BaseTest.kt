package com.example.otusproject_ermoshina.rules

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

open class BaseTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val ruleViewModelRules = ViewModelRules()

    @get:Rule
    val ruleInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get: Rule
    val rule = MockKRule(this)

    @Before
    open fun setUp() {
        hiltRule.inject()
    }

}