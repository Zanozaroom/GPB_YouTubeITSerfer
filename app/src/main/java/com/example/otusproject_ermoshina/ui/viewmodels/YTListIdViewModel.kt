package com.example.otusproject_ermoshina.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.otusproject_ermoshina.base.PlayListOfChannel
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import com.example.otusproject_ermoshina.ui.YTChannelPlayListsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YTListIdViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repoYouTube: RepositoryYouTube
) : ViewModel() {

    private val navArgs: YTChannelPlayListsArgs =
        YTChannelPlayListsArgs.fromSavedStateHandle(savedStateHandle)
    private val idChannel = navArgs.idChannel

    private var allPlayLists = mutableListOf<PlayListOfChannel>()
    private var _listYouTube = MutableLiveData<List<PlayListOfChannel>>()
    val listYouTube: LiveData<List<PlayListOfChannel>> = _listYouTube

    init {
        loadPlayLists(idChannel)
    }

    fun loadPlayLists(id: String, token: String = "") {
        viewModelScope.launch {
            val result = repoYouTube.loadChannelPlayLists(id, token).toVideosChannel()
            allPlayLists = ArrayList(allPlayLists)
            allPlayLists.addAll(result)
            _listYouTube.value = allPlayLists
            Log.i("RRR", _listYouTube.value.toString())
        }
    }

}

enum class ScreenState {
    LOAD, SUCCESS, ERROR
}
