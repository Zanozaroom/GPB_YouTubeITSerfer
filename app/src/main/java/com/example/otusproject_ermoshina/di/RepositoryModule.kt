package com.example.otusproject_ermoshina.di

import com.example.otusproject_ermoshina.domain.helpers.*
import com.example.otusproject_ermoshina.domain.repositories.RepositoryDataBase
import com.example.otusproject_ermoshina.domain.repositories.RepositoryNetwork
import com.example.otusproject_ermoshina.domain.repositories.RepositoryRoom
import com.example.otusproject_ermoshina.domain.repositories.RepositoryYouTube
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

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
    fun bindSearchLoader(helper: SearchLoadImpl): SearchLoad
    @Binds
    fun bindHelperPlayListLoader(helper: PlayListLoadImpl): PlayListLoad
    @Binds
    fun bindHelperVideoLoader(helper: VideoLoadImpl): VideoLoad
    @Binds
    fun bindHelperVideoListLoader(helper: VideoListLoadImpl): VideoListLoad

}

@Module
@InstallIn(SingletonComponent::class)
object ProviderRepository{
    @Provides
    @Named("Dispatchers.IO")
    fun provideCoroutineDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named("Dispatchers.Default")
    fun provideCoroutineDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default
}