package com.example.otusproject_ermoshina.ui.fragment.videolist

import android.util.Log
import androidx.lifecycle.*
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.domain.helpers.VideoListLoad
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
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
    private val _screenState = MutableLiveData<ViewModelResult<List<YTVideoList>>>()
    val screenState: LiveData<ViewModelResult<List<YTVideoList>>> = _screenState

    private val navArgs: YTVideoListFragmentArgs =
        YTVideoListFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val idVideoList = navArgs.idPlayList

    init {
        _screenState.value = LoadingViewModel
        loadVideoLists(idVideoList)
    }

    fun addToFavoritePlayList(idVideo: String) {
        viewModelScope.launch() {
            showToast(R.string.toastAddFavoritePlayList)
        }
    }

    fun loadMore() {
        viewModelScope.launch{
            try{
                val result = helper.getLoadMoreVideoList(idVideoList)
                if (result == null) {
                    showToast(R.string.toastAllVideoLoad)
                } else {
                    _screenState.value = SuccessViewModel(result)
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
                _screenState.value = EmptyResultViewModel
            } else  _screenState.value = SuccessViewModel(result)
        }catch (e:Exception){
                catchException(e)
        }}
        }
    fun addVideoToFavorite(idVideo:String){
        viewModelScope.launch{
          val deffered = async {
              helper.loadVideo(idVideo)
           }
            try{
                helper.saveVideo(deffered.await())
                showToast(R.string.toastAddFavoriteVideo)
            }catch (e:Exception){
                catchException(e)
                showToast(R.string.toastAddFavoriteFail)
            }
        }
    }

    private fun catchException(e:Exception){
        when(e){
            is NetworkLoadException -> {
                _screenState.value = ErrorLoadingViewModel
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            is DataBaseLoadException -> {
                _screenState.value = ErrorLoadingViewModel
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _screenState.value = ErrorLoadingViewModel
                Log.i("AAA", "Какая-то херобура")
            }
        }
    }
    }
