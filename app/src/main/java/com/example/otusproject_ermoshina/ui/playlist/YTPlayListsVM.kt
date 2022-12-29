package com.example.otusproject_ermoshina.ui.playlist

import android.util.Log
import androidx.lifecycle.*
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.base.NetworkLoadException
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.sources.helpers.PlayListLoad
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

    private val _screenState = MutableLiveData<LoadingResult<List<YTPlayList>>>()
    val screenState: LiveData<LoadingResult<List<YTPlayList>>> = _screenState
    lateinit var playlist: List<YTPlayList>

    init {
        loadPlayLists(idChannel)
    }
    fun loadMore() {
        viewModelScope.launch() {
            try {
                _screenState.value = LoadingResult.LoadingMore
                val result = helper.getLoadMoreListOfChannel(idChannel)
                if (result == null) {
                    showToast(R.string.toastAllVideoLoad)
                } else{
                    playlist = result
                    _screenState.value = LoadingResult.Success(result)
                }
            } catch (e: Exception) {
                catchException(e)
            }
        }
    }

    private fun loadPlayLists(id: String) {
        viewModelScope.launch() {
            _screenState.value = LoadingResult.Loading
            try {
                playlist = helper.getOneChannelAndVideoList(id)

                if(playlist.isEmpty()) _screenState.value = LoadingResult.Empty
                else  _screenState.value = LoadingResult.Success(playlist)

            }catch (e: Exception) {
                catchException(e)
            }

        }
    }

    fun addToFavoritePL(list: YTPlayList) {
        viewModelScope.launch {
            try{
                helper.addToFavoritePlayList(list)
                showToast(R.string.toastAddSeeLaterPlayList)
            }catch (e:DataBaseLoadException){
                showToast(R.string.messageErrorDataBaseSave)
            }
        }
    }

    private fun catchException(e:Exception){
        when (e) {
            is NetworkLoadException -> {
                _screenState.value = LoadingResult.Error
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            is DataBaseLoadException -> {
                _screenState.value = LoadingResult.Error
                showToast(R.string.messageRoomLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _screenState.value = LoadingResult.Error
                Log.i("AAA", "Непонятная ошибка, все сломалось в PlayListsVM $e")
            }
        }
    }
}
