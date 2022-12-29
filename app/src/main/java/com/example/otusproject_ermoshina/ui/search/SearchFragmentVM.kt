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

    private val navArgs: SearchFragmentArgs =
        SearchFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val query = navArgs.querty

    private val _state = MutableLiveData<LoadingResult<List<YTSearch>>>()
    val state: LiveData<LoadingResult<List<YTSearch>>> = _state

    init {
     load()
    }

    fun load() {
        _state.value = LoadingResult.Loading
        viewModelScope.launch() {
            try {
                _state.value =
                    LoadingResult.Success(helper.getResultSearch(query, maxResult = 5))
            } catch (e: Exception) {
                catchException(e)
            }
        }
    }
        fun loadMore() {
            viewModelScope.launch() {
                try {
                    _state.value = LoadingResult.LoadingMore
                    val result = helper.getLoadMoreResultSearch(query)
                    if (result.isNullOrEmpty()) {
                        showToast(R.string.toastAllVideoLoad)
                    } else _state.value =
                        LoadingResult.Success(result)
                } catch (e: Exception) {
                 catchException(e)
                }

            }
        }

    private fun catchException(e: Exception) {
        when (e) {
            is NetworkLoadException -> {
                _state.value = LoadingResult.Error
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            is DataBaseLoadException -> {
                _state.value = LoadingResult.Error
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _state.value = LoadingResult.Error
                Log.i("AAA", "Непонятная ошибка, все сломалось в PlayListsVM $e")
            }
        }
    }
}