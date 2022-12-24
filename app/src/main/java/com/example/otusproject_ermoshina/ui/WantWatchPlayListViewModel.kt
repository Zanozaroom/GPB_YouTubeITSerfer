package com.example.otusproject_ermoshina.ui

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.sources.RepositoryDataBase
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import com.example.otusproject_ermoshina.ui.base.MutableLiveEvent
import com.example.otusproject_ermoshina.ui.base.publishEvent
import com.example.otusproject_ermoshina.ui.base.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WantWatchPlayListViewModel @Inject constructor(
    private val ytRepositoryRetrofit: RepositoryYouTube,
    private val ytRepositoryYouTubeRoom: RepositoryDataBase
) : ViewModel() {
    private val _toastEvent = MutableLiveEvent<Int>()
    val toastEvent = _toastEvent.share()

    private var allPlayLists = mutableListOf<YTPlayListOfChannel>()
    private var _listYouTube = MutableLiveData<List<YTPlayListOfChannel>>()
    val listYouTube: LiveData<List<YTPlayListOfChannel>> = _listYouTube

    init{
        loadVideoPlayList()
    }

    fun loadVideoPlayList(){
        viewModelScope.launch {
            ytRepositoryYouTubeRoom.loadPlayList()
        }
    }
    private fun showToast(@StringRes messageRes: Int) {
        _toastEvent.publishEvent(messageRes)
    }
}