package com.example.otusproject_ermoshina.ui.screen.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.helpers.VideoLoad
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.domain.helpers.VideoLoadImpl
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVideoViewModel @Inject constructor(
    private val helper: VideoLoad
) : BaseViewModel() {
    private val _state = MutableLiveData<ViewModelResult<List<YTVideo>>>()
    val state: LiveData<ViewModelResult<List<YTVideo>>> = _state

    init{
        viewModelScope.launch {
            _state.value = LoadingViewModel
            helper.loadFavoriteVideo()
                .catch { exception ->
                    Log.i("AAA", " ошибка в loadFavoriteVideo $exception")
                    showToast(R.string.messageNetworkLoadException)
                }
                .collect{
                    _state.value = SuccessViewModel(it)
                }
        }
    }
    fun deleteVideo(video: YTVideo){
        viewModelScope.launch {
            try{
                helper.deleteVideo(video)
                showToast(R.string.toastRemoveFromFavorite)
            }catch (e:Exception){
                _state.value = ErrorLoadingViewModel
                showToast(R.string.toastRemoveFromFavoriteFail)
            }
        }
    }
}