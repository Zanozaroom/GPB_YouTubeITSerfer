package com.example.otusproject_ermoshina.di

import android.content.Context
import androidx.room.Room
import com.example.otusproject_ermoshina.sources.room.MyDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MyDataBase::class.java,
        "room_database"
    ).createFromAsset("otus_db.db").build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideYourDao(db: MyDataBase) = db.getChannelsDAO() // The reason we can implement a Dao for the database

}