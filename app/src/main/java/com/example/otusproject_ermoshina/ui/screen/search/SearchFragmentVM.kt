package com.example.otusproject_ermoshina.ui.screen.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.domain.model.YTSearchPaging
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

    private val _state = MutableLiveData<ViewModelResult<YTSearchPaging>>()
    val state: LiveData<ViewModelResult<YTSearchPaging>> = _state

    init {
     load()
    }

    fun load() {
        _state.value = LoadingViewModel
        viewModelScope.launch() {
            try {
                _state.value =
                    helper.getResultSearch(query,"", MAXRESULT_LOAD)
            } catch (e: Exception) {
                catchException(e)
            }
        }
    }
        fun loadMore(ytSearchPaging: YTSearchPaging) {
            viewModelScope.launch() {
                when{
                    ytSearchPaging.nextToken.isBlank() -> showToast(R.string.toastAllVideoLoad)
                    else -> try {
                        _state.value = helper.getLoadMoreResultSearch(ytSearchPaging, MAXRESULT_LOADMORE)

                    } catch (e: Exception) {
                        catchException(e)
                    }
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
    companion object{
        private const val MAXRESULT_LOAD = 6
        private const val MAXRESULT_LOADMORE = 4
    }
}