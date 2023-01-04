package com.example.otusproject_ermoshina.servise.retrofit

import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideoResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ChannelPlayListResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YTApi {
    @GET("videos")
    suspend fun getOneVideo(
        @Query("part") part: String,
        @Query("id") id: String,
        @Query("key") key: String
    ): Response<ModelLoadVideoResponse>

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
    ): Response<ModelLoadListVideosResponse>

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
    ): Response<ChannelPlayListResponse>

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
    ): Response<ModelSearchResponse>
}