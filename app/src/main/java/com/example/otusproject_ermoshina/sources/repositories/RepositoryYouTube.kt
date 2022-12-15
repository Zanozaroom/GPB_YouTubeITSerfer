package com.example.otusproject_ermoshina.sources.repositories

import android.util.Log
import com.example.otusproject_ermoshina.base.ChannelAndListVideos
import com.example.otusproject_ermoshina.sources.retrofit.BaseYouTubeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryYouTube(
    private val retrofitHelper: BaseYouTubeHelper
) {

    suspend fun loadVideoList() = withContext(Dispatchers.IO) {
        val response = retrofitHelper.getListVideos(PART, MAXRESULT, PLAY_LIST_ID, KEY)
        Log.i("AAA", "Start !")
        if (response.isSuccessful) {
            Log.i("AAA", "Start")
            response.body()?.toVideoList()
        } else {
            throw Exception("Result")
        }
    }

    suspend fun loadChannelPlayLists(channelId: String, nextTokenL: String = NEXT_TOKEN):ChannelAndListVideos = withContext(Dispatchers.IO) {

        val response = retrofitHelper.getListChannels(
            PART,
            MAXRESULT,
            nextTokenL,
            channelId,
            KEY
        )
        Log.i("AAA", "Start ! loadChannelPlayLists")
        if (response.isSuccessful) {
         response.body()!!.toChannelAndListVideos()
        } else {
            throw Exception("Result")
        }
    }


    companion object {
        const val KEY = "AIzaSyCbZT5ncVXel3T2mCW4MqjzGwPZIvojc_I"

        const val PART = "snippet"
        const val ID = "Ks-_Mh1QhMc"

        const val MAXRESULT = 7
        const val PLAY_LIST_ID = "PLxizNdMtXgxo0y4n-jK_YrQNrI4sPoDFo"
        const val NEXT_TOKEN = ""

    }
}