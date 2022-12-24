package com.example.otusproject_ermoshina.di

import com.example.otusproject_ermoshina.sources.*
import com.example.otusproject_ermoshina.sources.helpers.*
import com.example.otusproject_ermoshina.sources.repositories.RepositoryRoom
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindRepositoryYoutube(
        repositoryYouTube: RepositoryYouTube
    ): RepositoryNetwork

    @Binds
    fun bindRepositoryRoom(repositoryYouTubeRoom: RepositoryRoom): RepositoryDataBase

}

@Module
@InstallIn(ViewModelComponent::class)
interface Helpers {

    @Binds
    fun bindSearchLoader(helper: SearchLoadImp): SearchLoad
    @Binds
    fun bindHelperPlayListLoader(helper: PlayListLoadImpl): PlayListLoad
    @Binds
    fun bindHelperVideoLoader(helper: VideoLoadImpl): VideoLoad
    @Binds
    fun bindHelperVideoListLoader(helper: VideoListLoadImpl): VideoListLoad

}