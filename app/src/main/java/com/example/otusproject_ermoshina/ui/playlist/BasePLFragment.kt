package com.example.otusproject_ermoshina.ui.playlist

import android.view.View
import androidx.fragment.app.Fragment
import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.databinding.FragmentPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint

interface ActionPL {
    fun openPL(idList: String)
    fun removeFavorite(list: YTPlayList)
    fun addToFavoritePL(list: YTPlayList)
}
@AndroidEntryPoint
abstract class BasePLFragment : Fragment(), ActionPL {
    abstract var binding: FragmentPlaylistBinding

    fun showError() {
        with(binding) {
            progressBar.visibility = View.GONE
            recyclerVideoList.visibility = View.GONE
            buttonErrorLoad.visibility = View.VISIBLE
            messageErrorLoad.visibility = View.VISIBLE
        }
    }

    fun showResult() {
        with(binding) {
            progressBar.visibility = View.GONE
            recyclerVideoList.visibility = View.VISIBLE
            buttonErrorLoad.visibility = View.GONE
            messageErrorLoad.visibility = View.GONE
        }
    }

    fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            recyclerVideoList.visibility = View.GONE
            buttonErrorLoad.visibility = View.GONE
            messageErrorLoad.visibility = View.GONE
        }
    }

    fun showEmpty() {
        with(binding) {
            gropeError.visibility = View.GONE
            gropeResult.visibility = View.GONE
            progressBar.visibility = View.GONE
            imageNotData.visibility = View.VISIBLE
        }
    }
}