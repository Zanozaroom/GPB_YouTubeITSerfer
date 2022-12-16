package com.example.otusproject_ermoshina.di

//import com.example.otusproject_ermoshina.RepositoriesBase
import com.example.otusproject_ermoshina.sources.retrofit.YTApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object  NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun createHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor).protocols(mutableListOf(Protocol.HTTP_1_1)).build()

    }
    @Provides
    @Singleton
    fun createRetrofit(client: OkHttpClient, parser: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(parser))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun getApiClient(retrofit: Retrofit): YTApi {
        return retrofit.create(YTApi::class.java)
    }
    @Provides
    @Singleton
    fun createInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

      private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    }
//.baseUrl(BuildConfig.BASE_URL)