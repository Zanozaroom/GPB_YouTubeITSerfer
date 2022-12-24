package com.example.otusproject_ermoshina.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.DataBaseLoadException
import com.example.otusproject_ermoshina.base.NetworkLoadException
import com.example.otusproject_ermoshina.base.YTSearchAndTitle
import com.example.otusproject_ermoshina.sources.helpers.SearchLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val helper: SearchLoad
) : BaseViewModel() {
    lateinit var  job :Job





    private val _state = MutableLiveData<LoadingResult<List<YTSearchAndTitle>>>()
    val state: LiveData<LoadingResult<List<YTSearchAndTitle>>> = _state

    init {
        excLoad()
    }

    fun excLoad() {
        _state.value = LoadingResult.Loading
        viewModelScope.launch()
       {
           try{
            _state.value =
                LoadingResult.Success(helper.getResultSearchAndTitleFirstStart())
        } catch (e:Exception){
            when(e){
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

                    Log.i("AAA", "Какая-то херобура")
                }
            }



        }}
    }

}