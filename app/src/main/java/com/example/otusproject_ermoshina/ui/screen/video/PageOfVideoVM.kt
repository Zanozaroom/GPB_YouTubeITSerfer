package com.example.otusproject_ermoshina.ui.screen.video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
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

    private val navArgs: PageOfVideoFragmentArgs = PageOfVideoFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val idVideo = navArgs.idVideo


    init {
        loadVideo(idVideo)

    }
    fun tryLoad(){
        loadVideo(idVideo)
    }
    fun addVideoToFavorite(video:YTVideo){
        viewModelScope.launch{
            try{
                helper.saveVideo(video)
                showToast(R.string.toastAddFavoriteVideo)
            }
            catch (e:DataBaseLoadException){
                Log.i("AppExceptionsBase", "addVideoToFavorite ${e.sayException()}")
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
                catchException(e)
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

