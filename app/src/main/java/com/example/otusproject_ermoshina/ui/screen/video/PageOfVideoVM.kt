package com.example.otusproject_ermoshina.ui.screen.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.screen.video.PageOfVideoFragment.Companion.ARGS_ID_VIDEO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageOfVideoVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val helper: VideoLoad
) : BaseViewModel() {
    private val _screenState = MutableLiveData<ViewModelResult<YTVideo>>()
    val screenState: LiveData<ViewModelResult<YTVideo>> = _screenState

    private val args = savedStateHandle.get<String>(ARGS_ID_VIDEO)
    val idVideo = args


    init {
        loadVideo(idVideo!!)

    }
    fun tryLoad(){
        loadVideo(idVideo!!)
    }
    fun addVideoToFavorite(video:YTVideo){
        viewModelScope.launch{
            try{
                helper.saveVideo(video)
                showToast(R.string.toastAddFavoriteVideo)
            }
            catch (e:DataBaseLoadException){
                showToast(R.string.toastAddFavoriteFail)
            }
        }
    }

    private fun loadVideo(idVideo:String) {
        _screenState.value = LoadingViewModel
        viewModelScope.launch{
            try{
            _screenState.value = helper.loadingYTVideo(idVideo)
        }catch (e:Exception){
                _screenState.value = ErrorLoadingViewModel
                catchException(e)
        }
        }
    }
}

