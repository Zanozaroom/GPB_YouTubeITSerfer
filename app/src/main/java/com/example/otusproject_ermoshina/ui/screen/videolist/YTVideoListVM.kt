package com.example.otusproject_ermoshina.ui.screen.videolist

import androidx.lifecycle.*
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.helpers.VideoListLoad
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.screen.videolist.YTVideoListFragment.Companion.ARGS_VIDEO_LIST_ID
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val _screenState = MutableLiveData<ViewModelResult<YTVideoListPaging>>()
    val screenState: LiveData<ViewModelResult<YTVideoListPaging>> = _screenState

    private val args = savedStateHandle.get<String>(ARGS_VIDEO_LIST_ID)
    private val idVideoList = args

    init {
        loadVideoLists(idVideoList!!)
    }

    fun loadMore(ytVideoListPaging: YTVideoListPaging) {
        viewModelScope.launch {
            if (ytVideoListPaging.nextToken.isBlank()) {
                showToast(R.string.toastAllVideoLoad)
            } else {
                try {
                    val result = helper.getLoadMoreVideoList(ytVideoListPaging, MAX_RESULT_LOAD_MORE)
                    if(result is ErrorLoadingViewModel)
                        showToast(R.string.messageNetworkLoadException)
                    else _screenState.value = result
                } catch (e: Exception) {
                    catchException(e)
                }
            }
        }
    }

    fun tryLoad() {
        loadVideoLists(idVideoList!!)
    }

    private fun loadVideoLists(idPlayList: String) {
        _screenState.value = LoadingViewModel
        viewModelScope.launch {
            try {
                _screenState.value = helper.getVideoList(idPlayList, TOKEN_NULL, MAX_RESULT_LOAD)
            } catch (e: Exception) {
                _screenState.value = ErrorLoadingViewModel
                catchException(e)
            }
        }
    }

    fun addVideoToFavorite(idVideo: String) {
        viewModelScope.launch {
            val differed = async {
                helper.loadVideoFromYouTubeForSave(idVideo)
            }
            try {
                val resultLoadingVideo = differed.await()
                if(resultLoadingVideo == null)  showToast(R.string.toastAddFavoriteFail)
                else helper.saveVideo(resultLoadingVideo)
                showToast(R.string.toastAddFavoriteVideo)
            } catch (e: Exception) {
                catchException(e)
                showToast(R.string.toastAddFavoriteFail)
            }
        }
    }

    companion object {
        private const val MAX_RESULT_LOAD = 6
        private const val MAX_RESULT_LOAD_MORE = 4
        private const val TOKEN_NULL = ""
    }
}
