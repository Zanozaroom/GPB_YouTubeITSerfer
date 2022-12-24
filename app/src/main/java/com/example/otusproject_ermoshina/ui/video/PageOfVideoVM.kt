package com.example.otusproject_ermoshina.ui.video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.base.NetworkLoadException
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.sources.helpers.VideoLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageOfVideoVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val helper: VideoLoad
) : BaseViewModel() {
    private val _screenState = MutableLiveData<LoadingResult<YTVideo>>()
    val screenState: LiveData<LoadingResult<YTVideo>> = _screenState

    private val navArgs: PageOfVideoArgs = PageOfVideoArgs.fromSavedStateHandle(savedStateHandle)
    private val idVideo = navArgs.idVideo

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
        loadVideo(idVideo)

    }
    fun tryLoad(){
        loadVideo(idVideo)
    }
    private fun loadVideo(idVideo:String) {
        _screenState.value = LoadingResult.Loading
        viewModelScope.launch(coroutineExceptionHandler) {
            _screenState.value = LoadingResult.Success(helper.getLoadOneVideo(idVideo))
        }
    }
}

