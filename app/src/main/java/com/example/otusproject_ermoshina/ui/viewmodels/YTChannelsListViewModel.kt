package com.example.otusproject_ermoshina.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.otusproject_ermoshina.RepositoriesBase
import com.example.otusproject_ermoshina.base.Channel
import com.example.otusproject_ermoshina.base.ChannelAndListVideos
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTubeRoom
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class YTChannelsListViewModel(
    private val repoYouTubeRoom: RepositoryYouTubeRoom,
    private val repoYouTube: RepositoryYouTube
) : ViewModel() {
    private val listPlayList = mutableListOf<ChannelAndListVideos>()
    private val _listETChannels = MutableLiveData<List<Channel>>()
    val listETChannels: LiveData<List<Channel>> = _listETChannels

    private val _listChannelsLoad = MutableLiveData<List<ChannelAndListVideos>>()
    val listChannelsLoad: LiveData<List<ChannelAndListVideos>> = _listChannelsLoad
    init {
        //получила список каналов
        viewModelScope.launch {
             launch {
                repoYouTubeRoom.loadListChannels().collect {
                    _listETChannels.value = it
                        launch() {
                            //теперь нужно для этого списка подгрузить список плейлистов
                            var i = 0
                            listETChannels.value!!.forEach {
                                val ch = (repoYouTube.loadChannelPlayLists(it.idChannel))
                                if(ch.listVideos.isEmpty() == false){
                                    listPlayList.add(ch)
                                    Log.i("AAA", "listPlayList "+listPlayList[i].toString())
                                    i+=1
                                    Log.i("AAA", "idChannel "+it.idChannel)
                                }
                             else{
                                    Log.i("AAA", " ПУСТОЙ idChannel "+it.idChannel)
                             }

                                //  listPlayList.add(repoYouTube.loadChannelPlayLists(it.idChannel))
                            }
                            _listChannelsLoad.value = listPlayList
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

