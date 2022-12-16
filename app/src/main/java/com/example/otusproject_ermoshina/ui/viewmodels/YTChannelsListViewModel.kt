package com.example.otusproject_ermoshina.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
//import com.example.otusproject_ermoshina.RepositoriesBase
import com.example.otusproject_ermoshina.base.*
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTubeRoom
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

sealed class ChannelsListScreenState {
    data class Success(val data: List<ChannelAndListVideos>) : ChannelsListScreenState()
    data class Error(val message: String) : ChannelsListScreenState()
    object Loading : ChannelsListScreenState()
}
@HiltViewModel
class YTChannelsListViewModel @Inject constructor(
    private val repoYouTubeRoom: RepositoryYouTubeRoom,
    private val repoYouTube: RepositoryYouTube
) : ViewModel() {
    private var _listPlayList = mutableListOf<ChannelAndListVideos>()
    private var _listRoomBase = mutableListOf<Channel>()
    private val _screenState = MutableLiveData<ChannelsListScreenState>()
    val screenState: LiveData<ChannelsListScreenState> = _screenState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.i("AppExceptionsBase", "Handle $exception in CoroutineExceptionHandler")
    }

    init {
        loadStart()
    }
        fun loadStart(){
           viewModelScope.launch(coroutineExceptionHandler) {
               _screenState.value = ChannelsListScreenState.Loading
               supervisorScope {
                   val jobRoom = launch {
                       try {
                           _listRoomBase.addAll(repoYouTubeRoom.loadListChannels())
                       } catch (e: AppExceptionsBase) {
                           Log.i("AppExceptionsBase", e.sayException())
                           viewModelScope.cancel()
                           _screenState.value =
                               ChannelsListScreenState.Error("Ошибка загрузки, попробуйте еще раз")
                       }
                   }
                   val jobRetrofit = launch {
                       jobRoom.join()
                       Log.i("AppExceptionsBase", "launch(job0)")
                       _listRoomBase.forEach {
                           try {
                               Log.i("AppExceptionsBase", "launch(job0) try")
                               val channelPlayList = repoYouTube.loadChannelPlayLists(it.idChannel)
                               Log.i("AppExceptionsBase", "channelPlayList $channelPlayList")
                               if (!channelPlayList.listVideos.isEmpty()) {
                                   _listPlayList = ArrayList(_listPlayList)
                                   _listPlayList.add(channelPlayList)
                                   _screenState.value =
                                       ChannelsListScreenState.Success(_listPlayList)
                                   // _listChannelsLoad.postValue(listPlayList)
                               }
                           } catch (e: AppExceptionsBase) {
                               Log.i("AppExceptionsBase", e.sayException())
                               if (e is RetrofitAbsoluteLoadException) {
                                   _screenState.value =
                                       ChannelsListScreenState.Error("Ошибка загрузки, попробуйте еще раз")
                                   viewModelScope.cancel()
                               } else Log.i("AppExceptionsBase", "Strange Exception jobRetrofit $e")
                           }
                       }
                   }
               }
           }
        }

}