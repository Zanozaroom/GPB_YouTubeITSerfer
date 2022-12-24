package com.example.otusproject_ermoshina.ui.videolist

import android.util.Log
import androidx.lifecycle.*
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.NetworkLoadException
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.base.YTVideoList
import com.example.otusproject_ermoshina.sources.helpers.VideoListLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel, отвечающая за загрузку и отображение всех видео из конкретного плейлиста(загрузка по ID плейлиста)
 */

@HiltViewModel
class VideoListVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val helper: VideoListLoad
) : BaseViewModel() {
    private val _screenState = MutableLiveData<LoadingResult<List<YTVideoList>>>()
    val screenState: LiveData<LoadingResult<List<YTVideoList>>> = _screenState

    private val navArgs: VideoListArgs =
        VideoListArgs.fromSavedStateHandle(savedStateHandle)
    private val idVideoList = navArgs.idPlayList

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is DataBaseLoadException -> {
                viewModelScope.cancel()
                Log.i("AppExceptionsBase", "Handle ${exception.sayException()}")
                _screenState.value =
                    LoadingResult.Error
            }
            is NetworkLoadException -> {
                viewModelScope.cancel()
                Log.i("AppExceptionsBase", "Handle ${exception.sayException()}")
                _screenState.value =
                    LoadingResult.Error
            }
        }
    }

    init {
        _screenState.value = LoadingResult.Loading
        loadVideoLists(idVideoList)
    }

    fun addSeeLater(idVideo: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            //TODO добавить в рум таблицу с видео на потом
            // repoRoomYouTube.addPlayListSeeLater(idPlayList)
            showToast(R.string.toastAddSeeLaterPlayList)
        }
    }

    fun loadMore() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = helper.getLoadMoreVideoList(idVideoList)
            if (result == null) {
                showToast(R.string.toastAllVideoLoad)
            } else {
                _screenState.value = LoadingResult.Success(result)
            }
        }
    }
    fun tryLoad(){
        loadVideoLists(idVideoList)
    }
    private fun loadVideoLists(idPlayList: String){
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = helper.getListVideoByIdPlayList(idPlayList)
            if(result == null) {
                showToast(R.string.toastPlayListNotPublic)
                _screenState.value = LoadingResult.Error
            } else  _screenState.value = LoadingResult.Success(result)
        }
        }
    }
