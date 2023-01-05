package com.example.otusproject_ermoshina.ui.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.domain.model.YTMainFragmentData
import com.example.otusproject_ermoshina.domain.helpers.SearchLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val helper: SearchLoad
) : BaseViewModel() {

    private val _state = MutableLiveData<ViewModelResult<List<YTMainFragmentData>>>()
    val state: LiveData<ViewModelResult<List<YTMainFragmentData>>> = _state

    init {
        loadData()
    }

    fun loadData() {
        _state.value = LoadingViewModel
        viewModelScope.launch()
       {
           try{
            _state.value = helper.getMainFragmentPage(MAXRESULT)
        } catch (e:Exception){
               _state.value = ErrorLoadingViewModel
               catchException(e)
        }
        }
    }
    companion object{
        const val MAXRESULT = 4
    }

}