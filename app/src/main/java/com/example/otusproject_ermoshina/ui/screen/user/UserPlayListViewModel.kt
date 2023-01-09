package com.example.otusproject_ermoshina.ui.screen.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.domain.helpers.PlayListLoad
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPlayListViewModel @Inject constructor(
    private val helper: PlayListLoad
) : BaseViewModel() {
    private val _state = MutableLiveData<ViewModelResult<List<YTPlayList>>>()
    val state: LiveData<ViewModelResult<List<YTPlayList>>> = _state

    init{
        viewModelScope.launch {
            _state.value = LoadingViewModel
            helper.loadFavoritePlayList()
                .catch {
                    _state.value = ErrorLoadingViewModel
                    showToast(R.string.messageNetworkLoadException)
                }
                .collect{
                    if(it.isEmpty()) _state.value = EmptyResultViewModel
                    else _state.value = SuccessViewModel(it)
                }
        }
    }

    fun deletePlayList(idPlayList: String){
        viewModelScope.launch {
            try{
                helper.deletePlayListFromFavorite(idPlayList)
                showToast(R.string.toastRemoveFromFavorite)
            }catch (e:Exception){
                showToast(R.string.toastAddFavoriteFail)
            }
        }
        }
    }

