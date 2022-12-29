package com.example.otusproject_ermoshina.ui.fragment.playlist

import android.util.Log
import androidx.lifecycle.*
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.domain.model.YTChannelAndPlayList
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class YTPlayListsVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val helper: PlayListLoad
) : BaseViewModel() {

    private val navArgs: YTPlayListFragmentArgs =
        YTPlayListFragmentArgs.fromSavedStateHandle(savedStateHandle)
    val idChannel = navArgs.idChannel


    private val _screenState = MutableLiveData<ViewModelResult<YTChannelAndPlayList>>()
    val screenState: LiveData<ViewModelResult<YTChannelAndPlayList>> = _screenState

    init {
        firstLoadPlayList(idChannel)
    }

    fun loadMore(ytChannelAndPlayList: YTChannelAndPlayList) {
        viewModelScope.launch() {
            if (ytChannelAndPlayList.nextToken.isBlank()) {
                _screenState.value = NotMoreLoadingViewModel
                showToast(R.string.toastAllVideoLoad)
            } else {
                try {
                    _screenState.value = helper.loadChannelAndPlayList(
                        ytChannelAndPlayList.idChannel,
                        ytChannelAndPlayList.nextToken,
                        MAXRESULT_LOADMORE
                    )
                } catch (e: Exception) {
                    catchException(e)
                }
            }
        }
    }

    private fun firstLoadPlayList(idChannel: String) {
        viewModelScope.launch() {
            _screenState.value = LoadingViewModel
            try {
                _screenState.value = helper.loadChannelAndPlayList(idChannel, EMPTY_TOKEN, MAXRESULT)
            } catch (e: Exception) {
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

    private fun catchException(e: Exception) {
        when (e) {
            is NetworkLoadException -> {
                _screenState.value = ErrorLoadingViewModel
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            is DataBaseLoadException -> {
                _screenState.value = ErrorLoadingViewModel
                showToast(R.string.messageRoomLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _screenState.value = ErrorLoadingViewModel
                Log.i("AAA", "Непонятная ошибка, все сломалось в PlayListsVM $e")
            }
        }
    }

    companion object {
        const val MAXRESULT = 6
        const val MAXRESULT_LOADMORE = 4
        const val EMPTY_TOKEN = ""

    }
}
