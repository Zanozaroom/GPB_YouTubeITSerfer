package com.example.otusproject_ermoshina

import android.content.Context
import androidx.room.Room
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTubeRoom
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import com.example.otusproject_ermoshina.sources.retrofit.BaseYouTubeHelper
import com.example.otusproject_ermoshina.sources.retrofit.YTApi
import com.example.otusproject_ermoshina.sources.room.MyDataBase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RepositoriesBase {
    private lateinit var appContext: Context

    fun init(c: Context) {
        appContext = c
    }



    private val database: MyDataBase by lazy<MyDataBase> {
        Room.databaseBuilder(
            appContext.applicationContext,
            MyDataBase::class.java,
            "room_database"
        )
            .createFromAsset("otus_db.db")
            .build()
    }


    val youtubeRepoRoom: RepositoryYouTubeRoom by lazy {
        RepositoryYouTubeRoom(database.getChannelsDAO())
    }

    val youtubeRepo: RepositoryYouTube by lazy {
        RepositoryYouTube(getYTApi())
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    }

    private fun getYTApi(): BaseYouTubeHelper {
        val yTApi = retrofit.create(YTApi::class.java)
        return BaseYouTubeHelper(yTApi)
    }

    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
}