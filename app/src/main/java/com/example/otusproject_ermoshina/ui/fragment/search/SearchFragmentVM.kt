package com.example.otusproject_ermoshina.ui.fragment.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.model.YTSearch
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _state = MutableLiveData<ViewModelResult<List<YTSearch>>>()
    val state: LiveData<ViewModelResult<List<YTSearch>>> = _state

    init {
     load()
    }

    fun load() {
        _state.value = LoadingViewModel
        viewModelScope.launch() {
            try {
                _state.value =
                    SuccessViewModel(helper.getResultSearch(query, maxResult = 5))
            } catch (e: Exception) {
                catchException(e)
            }
        }
    }
        fun loadMore() {
            viewModelScope.launch() {
                try {
                    val result = helper.getLoadMoreResultSearch(query)
                    if (result.isNullOrEmpty()) {
                        showToast(R.string.toastAllVideoLoad)
                    } else _state.value =
                        SuccessViewModel(result)
                } catch (e: Exception) {
                 catchException(e)
                }

            }
        }

    private fun catchException(e: Exception) {
        when (e) {
            is NetworkLoadException -> {
                _state.value = ErrorLoadingViewModel
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            is DataBaseLoadException -> {
                _state.value = ErrorLoadingViewModel
                showToast(R.string.messageNetworkLoadException)
                Log.i("AAA", e.sayException())
            }
            else -> {
                _state.value = ErrorLoadingViewModel
                Log.i("AAA", "Непонятная ошибка, все сломалось в PlayListsVM $e")
            }
        }
    }
}