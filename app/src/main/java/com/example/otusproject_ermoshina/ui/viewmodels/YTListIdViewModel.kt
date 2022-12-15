package com.example.otusproject_ermoshina.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.otusproject_ermoshina.RepositoriesBase
import com.example.otusproject_ermoshina.base.PlayListOfChannel
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import kotlinx.coroutines.launch

class YTListIdViewModel (private val repoYouTube: RepositoryYouTube) : ViewModel() {
    private var allPlayLists = mutableListOf<PlayListOfChannel>()
    private var _listYouTube = MutableLiveData<List<PlayListOfChannel>>()
    val listYouTube: LiveData<List<PlayListOfChannel>> = _listYouTube


    fun loadPlayLists(id: String, token: String = ""){
        viewModelScope.launch {
            val result = repoYouTube.loadChannelPlayLists(id, token).toVideosChannel()
            allPlayLists = ArrayList(allPlayLists)
            allPlayLists.addAll(result)
            _listYouTube.value = allPlayLists
            Log.i("RRR", _listYouTube.value.toString())
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                //initial database
                RepositoriesBase.init(application.applicationContext)
                //get repository of Exercise
                val repoYT = RepositoriesBase.youtubeRepo
                //
                // Create a SavedStateHandle for this ViewModel from extras
                //  val savedStateHandle = extras.createSavedStateHandle()

                return YTListIdViewModel(
                    repoYT,
                    // savedStateHandle
                ) as T
            }
        }

    }
}

enum class ScreenState {
    LOAD, SUCCESS, ERROR
}
