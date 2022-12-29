package com.example.otusproject_ermoshina.ui.playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.sources.helpers.PlayListLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPlayListViewModel @Inject constructor(
    private val helper: PlayListLoad
) : BaseViewModel() {
    private val _state = MutableLiveData<LoadingResult<List<YTPlayList>>>()
    val state: LiveData<LoadingResult<List<YTPlayList>>> = _state

    init{
        viewModelScope.launch {
            _state.value = LoadingResult.Loading
            helper.loadFavoritePlayList()
                .catch { exception ->
                    _state.value = LoadingResult.Error
                    Log.i("AAA", " ошибка в loadFavoritePlayList $exception")
                    showToast(R.string.messageNetworkLoadException)
                }
                .collect{
                    _state.value = LoadingResult.Success(it)
                }
        }
    }

    fun deletePlayList(playList:YTPlayList){
        viewModelScope.launch {
            try{
                helper.deletePlayListFromFavorite(playList)
            }catch (e:Exception){
                catchException(e)
            }
        }
        }

    private fun catchException(e:Exception){
        when (e) {
            is DataBaseLoadException -> {
                _state.value = LoadingResult.Error
                showToast(R.string.messageRoomLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _state.value = LoadingResult.Error
                Log.i("AAA", "Непонятная ошибка, все сломалось в UserPlayListViewModel $e")
            }
        }
    }
    }

