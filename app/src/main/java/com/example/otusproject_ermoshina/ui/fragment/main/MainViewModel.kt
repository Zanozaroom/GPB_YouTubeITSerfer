package com.example.otusproject_ermoshina.ui.fragment.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.DataBaseLoadException
import com.example.otusproject_ermoshina.domain.NetworkLoadException
import com.example.otusproject_ermoshina.domain.model.YTSearchAndTitle
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val helper: SearchLoad
) : BaseViewModel() {

    private val _state = MutableLiveData<ViewModelResult<List<YTSearchAndTitle>>>()
    val state: LiveData<ViewModelResult<List<YTSearchAndTitle>>> = _state

    init {
        excLoad()
    }

    fun excLoad() {
        _state.value = LoadingViewModel
        viewModelScope.launch()
       {
           try{
            _state.value =
                SuccessViewModel(helper.getResultSearchAndTitleFirstStart())
        } catch (e:Exception){
            when(e){
                is NetworkLoadException -> {
                    Log.i("AAA", "NetworkLoadException первый")
                    _state.value = ErrorLoadingViewModel
                    showToast(R.string.messageNetworkLoadException)
                    Log.i("AAA", e.sayException())
                }
                is DataBaseLoadException -> {
                    Log.i("AAA", "NetworkLoadException второй")
                    _state.value = ErrorLoadingViewModel
                    showToast(R.string.messageNetworkLoadException)
                    Log.i("AAA", e.sayException())
                }
                else -> {
                    _state.value = ErrorLoadingViewModel

                    Log.i("AAA", "Все сломалось в MainViewModel $e")
                }
            }



        }}
    }

}