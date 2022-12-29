package com.example.otusproject_ermoshina.servise.retrofit

import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideos
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideo
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelChannelPlayList
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YTApi {
    @GET("videos")
    suspend fun getOneVideo(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") key: String
    ): Response<ModelLoadVideo>

    /**
     * Запрос возвращает список всех видео из плейлиста
     * Ищет по Id плейлиста
     */
    @GET("playlistItems")
    suspend fun getListVideos(
        @Query("part") part: String,
        @Query("pageToken") pageToken: String,
        @Query("maxResults") maxRes: Int,
        @Query("playlistId") playlistId: String,
        @Query("key") key: String
    ): Response<ModelLoadListVideos>

    /**
     * Запрос возвращает список всех плейлистов канала.
     * Ищет по Id канала
     */
    @GET("playlists")
    suspend fun getListChannels(
        @Query("part") part: String,
        @Query("maxResults") maxRes: Int,
        @Query("pageToken") nextToken: String,
        @Query("channelId") channelId: String,
        @Query("key") key: String
    ): Response<ModelChannelPlayList>

    /**
     * Запрос возвращает список видео по поисковому запросу.
     * Ищет по запросу
     */
    @GET("search")
    suspend fun getResultSearch(
        @Query("part") part: String,
        @Query("maxResults") maxRes: Int,
        @Query("pageToken") nextToken: String,
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("safesearch") safeSearch:String
    ): Response<ModelSearch>
}