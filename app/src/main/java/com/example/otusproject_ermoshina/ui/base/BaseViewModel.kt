package com.example.otusproject_ermoshina.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

open class BaseViewModel:ViewModel() {

    sealed class LoadingResult<out T> {
        data class Success<out R>(val dataList: R) : LoadingResult<R>()
        object Empty : LoadingResult<Nothing>()
        object Error : LoadingResult<Nothing>()
        object Loading : LoadingResult<Nothing>()
        object LoadingMore: LoadingResult<Nothing>()
    }

    protected val _toastEvent = MutableLiveEvent<Int>()
    val toastEvent = _toastEvent.share()
    protected fun showToast(@StringRes messageRes: Int) {
        _toastEvent.publishEvent(messageRes)
    }
}
