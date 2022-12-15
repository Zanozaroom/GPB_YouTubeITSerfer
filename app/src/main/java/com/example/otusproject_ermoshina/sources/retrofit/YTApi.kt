package com.example.otusproject_ermoshina.sources.retrofit

import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadListVideos
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelLoadVideo
import com.example.otusproject_ermoshina.sources.retrofit.models.ModelChannelPlayList
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

    @GET("playlistItems")
    suspend fun getListVideos(
        @Query("part") part: String,
        @Query("maxResults") maxRes: Int,
        @Query("playlistId") playlistId: String,
        @Query("key") key: String
    ): Response<ModelLoadListVideos>

    @GET("playlists")
    suspend fun getListChannels(@Query("part") part :String,
                              @Query("maxResults") maxRes: Int,
                               @Query("nextPageToken") nextToken: String,
                              @Query("channelId") channelId: String,
                              @Query("key") key: String): Response<ModelChannelPlayList>
}
/*
------Простой запрос выгрузки одного видео
 //создаю сам объект
        val retroPost = ApiClientRetrofit.client

        val apiCall: YTApi = retroPost!!.create(YTApi::class.java)
        val request = apiCall.getOneVideo(PART,ID,KEY)
        request.enqueue(object :Callback<ModelLoadVideo>{
            override fun onResponse(
                call: Call<ModelLoadVideo>,
                response: Response<ModelLoadVideo>
            ) {
                if(response.isSuccessful){
                    val result: ModelLoadVideo = response.body()!!
                    Log.i("AAA", "Получен результат "+ result.toString())
                }else{
                    Log.i("AAA", "К серверу есть коннект, но какая-то ошибка получения")
                }
            }

            override fun onFailure(call: Call<ModelLoadVideo>, t: Throwable) {
                Log.i("AAA", "К серверу вообще не подключились "+ t.toString())
            }

        })
    }

    --- запрос выгрузки плей-листа
val retroPost = ApiClientRetrofit.client

        val apiCall: YTApi = retroPost!!.create(YTApi::class.java)
        val request = apiCall.getListVideos(PART, MAXRESULT, PLAY_LIST_ID, KEY)
        request.enqueue(object :Callback<ModelLoadListVideos>{
            override fun onResponse(
                call: Call<ModelLoadListVideos>,
                response: Response<ModelLoadListVideos>
            ) {
                if(response.isSuccessful){
                    val result: ModelLoadListVideos = response.body()!!
                    listVideo = result.toVideoList()!!
                    Log.i("AAA", "Получен результат "+ listVideo.joinToString())
                }else{
                    Log.i("AAA", "К серверу есть коннект, но какая-то ошибка получения")
                }
            }

            override fun onFailure(call: Call<ModelLoadListVideos>, t: Throwable) {
                Log.i("AAA", "К серверу вообще не подключились "+ t.toString())
            }

        })
    }

    companion object{
        const val PART = "snippet"
        const val ID = "Ks-_Mh1QhMc"
        const val KEY = "AIzaSyCbZT5ncVXel3T2mCW4MqjzGwPZIvojc_I"

        const val MAXRESULT = 7
        const val PLAY_LIST_ID = "PLxizNdMtXgxo0y4n-jK_YrQNrI4sPoDFo"

    }
 */