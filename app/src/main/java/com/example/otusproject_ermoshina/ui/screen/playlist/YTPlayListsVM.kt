package com.example.otusproject_ermoshina.ui.screen.playlist

import androidx.lifecycle.*
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.domain.model.YTPlayListPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.screen.playlist.YTPlayListFragment.Companion.ARGS_ID_CHANNEL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class YTPlayListsVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val helper: PlayListLoad
) : BaseViewModel() {

    val idChannel = savedStateHandle.get<String>(ARGS_ID_CHANNEL)


    private val _screenState = MutableLiveData<ViewModelResult<YTPlayListPaging>>()
    val screenState: LiveData<ViewModelResult<YTPlayListPaging>> = _screenState

    init {
        firstLoadPlayList(idChannel!!)
    }

    fun loadMore(ytChannelAndPlayList: YTPlayListPaging) {
        viewModelScope.launch {
            if (ytChannelAndPlayList.nextToken.isBlank()) {
                _screenState.value = NotMoreLoadingViewModel
                showToast(R.string.toastAllVideoLoad)
            } else {
                try {
                    _screenState.value = helper.loadMorePlayList(
                        ytChannelAndPlayList,
                        ytChannelAndPlayList.nextToken,
                        MAXRESULT_LOADMORE
                    )
                } catch (e: Exception) {
                    catchException(e)
                }
            }
        }
    }
    fun firstLoadPlayList(idChannel: String) {
        viewModelScope.launch {
            _screenState.value = LoadingViewModel
            try {
                _screenState.value = helper.firstLoadPlayList(idChannel, EMPTY_TOKEN, MAXRESULT)
            } catch (e: Exception) {
                _screenState.value = ErrorLoadingViewModel
                catchException(e)
            }
        }
    }

    fun addToFavoritePL(list: YTPlayList) {
        viewModelScope.launch {
            try {
                helper.addToFavoritePlayList(list)
                showToast(R.string.toastAddFavoritePlayList)
            } catch (e: DataBaseLoadException) {
                showToast(R.string.messageErrorDataBaseSave)
            }
        }
    }


    companion object {
        const val MAXRESULT = 6
        const val MAXRESULT_LOADMORE = 4
        const val EMPTY_TOKEN = ""

    }
}
