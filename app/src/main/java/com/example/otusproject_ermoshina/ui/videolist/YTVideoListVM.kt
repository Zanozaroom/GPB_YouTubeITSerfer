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
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel, отвечающая за загрузку и отображение всех видео из конкретного плейлиста(загрузка по ID плейлиста)
 */

@HiltViewModel
class YTVideoListVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val helper: VideoListLoad
) : BaseViewModel() {
    private val _screenState = MutableLiveData<LoadingResult<List<YTVideoList>>>()
    val screenState: LiveData<LoadingResult<List<YTVideoList>>> = _screenState

    private val navArgs: YTVideoListArgs =
        YTVideoListArgs.fromSavedStateHandle(savedStateHandle)
    private val idVideoList = navArgs.idPlayList

    init {
        _screenState.value = LoadingResult.Loading
        loadVideoLists(idVideoList)
    }

    fun addSeeLater(idVideo: String) {
        viewModelScope.launch() {

            showToast(R.string.toastAddSeeLaterPlayList)
        }
    }

    fun loadMore() {
        viewModelScope.launch{
            try{
                _screenState.value = LoadingResult.LoadingMore
                val result = helper.getLoadMoreVideoList(idVideoList)
                if (result == null) {
                    showToast(R.string.toastAllVideoLoad)
                } else {
                    _screenState.value = LoadingResult.Success(result)
                }
            }catch (e: Exception){
                catchException(e)
            }
        }
    }
    fun tryLoad(){
        loadVideoLists(idVideoList)
    }
    private fun loadVideoLists(idPlayList: String){
        viewModelScope.launch{
            try{
            val result = helper.getVideoList(idPlayList)
            if(result == null) {
                showToast(R.string.toastPlayListNotPublic)
                _screenState.value = LoadingResult.Error
            } else  _screenState.value = LoadingResult.Success(result)
        }catch (e:Exception){
                catchException(e)
        }}
        }

    private fun catchException(e:Exception){
        when(e){
            is NetworkLoadException -> {
                _screenState.value = LoadingResult.Error
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            is DataBaseLoadException -> {
                _screenState.value = LoadingResult.Error
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _screenState.value = LoadingResult.Error
                Log.i("AAA", "Какая-то херобура")
            }
        }
    }
    }
