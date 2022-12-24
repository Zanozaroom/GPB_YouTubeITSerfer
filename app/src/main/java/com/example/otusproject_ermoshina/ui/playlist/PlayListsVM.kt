package com.example.otusproject_ermoshina.ui.playlist

import android.util.Log
import androidx.lifecycle.*
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.base.NetworkLoadException
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.sources.helpers.PlayListLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayListsVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadingHelper: PlayListLoad
) : BaseViewModel() {

    private val navArgs: PlayListsArgs =
        PlayListsArgs.fromSavedStateHandle(savedStateHandle)
    val idChannel = navArgs.idChannel

    private val _screenState = MutableLiveData<LoadingResult<List<YTPlayListOfChannel>>>()
    val screenState: LiveData<LoadingResult<List<YTPlayListOfChannel>>> = _screenState
    lateinit var playlist: List<YTPlayListOfChannel>

    init {
        loadPlayLists(idChannel)
    }
    fun loadMore() {
        viewModelScope.launch() {
            try {
                val result = loadingHelper.getLoadMoreListOfChannel(idChannel)
                if (result == null) {
                    showToast(R.string.toastAllVideoLoad)
                } else{
                    playlist = result
                    _screenState.value = LoadingResult.Success(result)
                }
            } catch (e: Exception) {
                when (e) {
                    is NetworkLoadException -> {
                        showToast(R.string.messageNetworkLoadException)
                        Log.i("AAA", e.sayException())
                    }
                    else -> {
                        Log.i("AAA", "Непонятная ошибка, все сломалось в PlayListsVM $e")
                        showToast(R.string.messageStrangeException)
                    }
                }
            }
        }
    }

    private fun loadPlayLists(id: String) {
        viewModelScope.launch() {
            _screenState.value = LoadingResult.Loading
            try {
                playlist = loadingHelper.getOneChannelAndVideoList(id)

                if(playlist.isEmpty()) _screenState.value = LoadingResult.Empty
                else  _screenState.value = LoadingResult.Success(playlist)

            }catch (e: Exception) {
                when (e) {
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
                        Log.i("AAA", "Непонятная ошибка, все сломалось в PlayListsVM $e")
                    }
                }
            }

        }
    }

    fun addToFavoritePL(list: YTPlayListOfChannel) {
        viewModelScope.launch {
            loadingHelper.addToFavoritePlayList(list)
            showToast(R.string.toastAddSeeLaterPlayList)
        }
    }
}
