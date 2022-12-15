package com.example.otusproject_ermoshina.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.otusproject_ermoshina.RepositoriesBase
import com.example.otusproject_ermoshina.base.*
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTubeRoom
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import kotlinx.coroutines.*

sealed class ChannelsListScreenState {
    data class Success(val data: List<ChannelAndListVideos>) : ChannelsListScreenState()
    data class Error(val message: String) : ChannelsListScreenState()
    object Loading : ChannelsListScreenState()
}

class YTChannelsListViewModel(
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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                //get application
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                //initial database
                RepositoriesBase.init(application.applicationContext)
                //get repository of Exercise
                val repoYT = RepositoriesBase.youtubeRepoRoom
                val repoYTRetr = RepositoriesBase.youtubeRepo
                //
                // Create a SavedStateHandle for this ViewModel from extras
                //  val savedStateHandle = extras.createSavedStateHandle()

                return YTChannelsListViewModel(
                    repoYT, repoYTRetr,
                    // savedStateHandle
                ) as T
            }
        }
    }
}