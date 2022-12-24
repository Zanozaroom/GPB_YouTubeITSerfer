package com.example.otusproject_ermoshina.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.base.NetworkLoadException
import com.example.otusproject_ermoshina.base.YTSearch
import com.example.otusproject_ermoshina.sources.helpers.SearchLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentVM @Inject constructor(
    private val helper: SearchLoad,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is DataBaseLoadException -> {
                viewModelScope.cancel()
                Log.i("AppExceptionsBase", "Handle ${exception.sayException()}")
                _state.value = LoadingResult.Error
                showToast(R.string.messageRoomLoadException)
            }
            is NetworkLoadException -> {
                Log.i("AppExceptionsBase", "Handle ${exception.sayException()}")
                showToast(R.string.messageNetworkLoadException)
            }
            else -> {
                viewModelScope.cancel()
                Log.i("AppExceptionsBase", "Handle - другая ошибка ${exception}")
                _state.value = LoadingResult.Error
                showToast(R.string.messageNetworkLoadException)
            }
        }
    }

    private val navArgs: SearchFragmentArgs =
        SearchFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val query = navArgs.querty

    private val _state = MutableLiveData<LoadingResult<List<YTSearch>>>()
    val state: LiveData<LoadingResult<List<YTSearch>>> = _state

    init {
        _state.value = LoadingResult.Loading
        viewModelScope.launch(coroutineExceptionHandler) {
            _state.value =
                LoadingResult.Success(helper.getResultSearch(query, maxResult = 3))
        }
    }

    fun excLoad() {
        _state.value = LoadingResult.Loading
        viewModelScope.launch(coroutineExceptionHandler) {
            _state.value =
                LoadingResult.Success(helper.getResultSearch(query, maxResult = 3))
        }
    }
    fun loadMore(){
        _state.value = LoadingResult.Loading
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = helper.getLoadMoreResultSearch(query)
            if(result.isNullOrEmpty()){
                showToast(R.string.toastAllVideoLoad)
            } else _state.value =
                LoadingResult.Success(result)
        }
    }
}