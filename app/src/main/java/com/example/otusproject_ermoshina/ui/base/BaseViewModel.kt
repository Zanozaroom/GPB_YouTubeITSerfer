package com.example.otusproject_ermoshina.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException

open class BaseViewModel:ViewModel() {

    sealed class ViewModelResult<out T>
    data class SuccessViewModel<out R>(val dataViewModelResult: R) : ViewModelResult<R>()
    object EmptyResultViewModel : ViewModelResult<Nothing>()
    object ErrorLoadingViewModel : ViewModelResult<Nothing>()
    object LoadingViewModel : ViewModelResult<Nothing>()
    object NotMoreLoadingViewModel: ViewModelResult<Nothing>()


    protected val _toastEvent = MutableLiveEvent<Int>()
    val toastEvent = _toastEvent.share()
    fun showToast(@StringRes messageRes: Int) {
        _toastEvent.publishEvent(messageRes)
    }

    fun catchException(e: Exception){
        when(e){
            is NetworkLoadException -> {
                showToast(R.string.messageNetworkLoadException)
            }
            is DataBaseLoadException -> {
                showToast(R.string.messageRoomLoadException)
            }
            else -> {
                showToast(R.string.messageStrangeLoadException)
            }
        }
    }
}
