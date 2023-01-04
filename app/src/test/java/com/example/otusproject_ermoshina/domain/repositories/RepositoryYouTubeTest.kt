package com.example.otusproject_ermoshina.domain.repositories

import com.example.otusproject_ermoshina.domain.helpers.CreatorSearch
import com.example.otusproject_ermoshina.domain.helpers.CreatorVideo
import com.example.otusproject_ermoshina.domain.helpers.CreatorVideoList
import com.example.otusproject_ermoshina.servise.retrofit.YTApi
import com.example.otusproject_ermoshina.servise.retrofit.model.ChannelPlayListResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadListVideosResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelLoadVideoResponse
import com.example.otusproject_ermoshina.servise.retrofit.model.ModelSearchResponse
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class RepositoryYouTubeTest {
    val dummyCoroutineDispatcher = Dispatchers.Unconfined

    @get: Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var retrofit: YTApi

    lateinit var testRepositoryYouTube: RepositoryYouTube

    @Before
    fun init() {
        testRepositoryYouTube =
            RepositoryYouTube(retrofit, dummyCoroutineDispatcher)
    }

    @Test
    fun `loadChannelList CallsRetrofit GetSuccessResponse`() = runBlocking {
        val dataFromRetrofit = CreatorYouTubeRepository.createChannelPlayListResponse()
        val dataFromRepository = dataFromRetrofit.toChannelAndListVideos()

        coEvery {
            retrofit.getListChannels(any(), any(), any(), any(), any())
        } returns Response.success(dataFromRetrofit)

        val result = testRepositoryYouTube.loadChannelList("channelId", "token", 1)

        assertEquals(SuccessNetworkResult(dataFromRepository), result)
    }

    @Test
    fun `loadChannelList CallsRetrofit GetErrorResponse`() = runBlocking {
        val errorBody = Response.error<ChannelPlayListResponse>(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery {
            retrofit.getListChannels(any(), any(), any(), any(), any())
        } returns errorBody

        val result = testRepositoryYouTube.loadChannelList("channelId", "token", 1)

        assertEquals(ErrorNetworkResult, result)
    }

    @Test
    fun `getListVideos CallsRetrofit GetSuccessResponse`() = runBlocking {
        val dataFromRetrofit = CreatorVideoList.createModelLoadListVideos()

        coEvery {
            retrofit.getListVideos(any(), any(), any(), any(), any())
        } returns Response.success(dataFromRetrofit)

        val result = testRepositoryYouTube.getListVideos("playListId", "token", 1)

        assertEquals(SuccessNetworkResult(dataFromRetrofit), result)
    }

    @Test
    fun `getListVideos CallsRetrofit GetErrorResponse`() = runBlocking {
        val errorBody = Response.error<ModelLoadListVideosResponse>(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery {
            retrofit.getListVideos(any(), any(), any(), any(), any())
        } returns errorBody

        val result = testRepositoryYouTube.getListVideos("playListId", "token", 1)

        assertEquals(ErrorNetworkResult, result)
    }

    @Test
    fun `loadOneVideo CallsRetrofit GetSuccessResponse`() = runBlocking {
        val dataFromRetrofit = CreatorVideo.createModelLoadVideo()

        coEvery {
            retrofit.getOneVideo(any(), any(), any())
        } returns Response.success(dataFromRetrofit)

        val result = testRepositoryYouTube.loadOneVideo("idVideo")

        assertEquals(SuccessNetworkResult(dataFromRetrofit), result)
    }

    @Test
    fun `loadOneVideo CallsRetrofit GetErrorResponse`() = runBlocking {
        val errorBody = Response.error<ModelLoadVideoResponse>(
            400,
            "{\"key\":[\"somestuff\"]}".toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery {
            retrofit.getOneVideo(any(), any(), any())
        } returns errorBody

        val result = testRepositoryYouTube.getListVideos("idVideo", "token", 1)

        assertEquals(ErrorNetworkResult, result)
    }

    @Test
    fun `getResultSearch CallsRetrofit GetSuccessResponse`() = runBlocking {
        val dataFromRetrofit = CreatorSearch.createModelSearch()

        coEvery {
            retrofit.getResultSearch(any(), any(), any(), any(), any(), any())
        } returns Response.success(dataFromRetrofit)

        val result = testRepositoryYouTube.getResultSearch(
            "query", 1, "token", "safeSearch")

        assertEquals(SuccessNetworkResult(dataFromRetrofit), result)
    }

    @Test
    fun `getResultSearch CallsRetrofit GetErrorResponse`() = runBlocking {
        val errorBody = Response.error<ModelSearchResponse>(
            400,
            "{\"key\":[\"somestuff\"]}".toResponseBody("application/json".toMediaTypeOrNull())
        )

        coEvery {
            retrofit.getResultSearch(any(), any(), any(), any(), any(), any())
        } returns errorBody

        val result = testRepositoryYouTube.getResultSearch(
            "query", 1, "token", "safeSearch")

        assertEquals(ErrorNetworkResult, result)
    }
}