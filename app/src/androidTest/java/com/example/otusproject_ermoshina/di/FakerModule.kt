package com.example.otusproject_ermoshina.di

import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.domain.helpers.VideoListLoad
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FakerModule {
    @Provides
    @Singleton
    fun searchLoader(): SearchLoad {
        return mockk(relaxed = true)
    }
    @Provides
    @Singleton
    fun helperPlayListLoader(): PlayListLoad {
        return mockk(relaxed = true)
    }
    @Provides
    @Singleton
    fun helperVideoLoader(): VideoLoad {
        return mockk(relaxed = true)
    }
    @Provides
    @Singleton
    fun helperVideoListLoader(): VideoListLoad {
        return mockk(relaxed = true)
    }
}