package com.example.otusproject_ermoshina.ui.user_pageview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.databinding.FragmentPlaylistBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.LoadingResult
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.ui.playlist.AdapterPlayList
import com.example.otusproject_ermoshina.ui.playlist.BasePLFragment
import com.example.otusproject_ermoshina.ui.playlist.UserPlayListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserPlayList : BasePLFragment() {

    companion object {
        fun newInstance() = UserPlayList()
    }
    private val adapterVideoIdList: AdapterPlayList by lazy {
        AdapterPlayList(this@UserPlayList, this@UserPlayList)
    }
    private val viewModel: UserPlayListViewModel by viewModels()

    override lateinit var binding: FragmentPlaylistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container,false)
        viewModel.state.observe(viewLifecycleOwner){
            stateScreen(it)
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner){
            Toast.makeText(requireActivity(),it, Toast.LENGTH_SHORT).show()
        }
        initAdapterSetting()
        return binding.root
    }

    override fun openPL(idList: String) {
       // TODO("Not yet implemented")
    }

    override fun removeFavorite(list: YTPlayList) {
      viewModel.deletePlayList(list)
    }

    private fun initAdapterSetting(){
        binding.recyclerVideoList.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = adapterLayoutManager

        }
    }
    private fun stateScreen(it: LoadingResult<List<YTPlayList>>) {
    when (it){
        LoadingResult.Error -> {
            showError()
        }
        is LoadingResult.Success -> {
            adapterVideoIdList.submitList(it.dataList)
            showResult()
        }
        LoadingResult.Loading -> {
            showLoading()
        }
        else -> {showLoading()}
    }
    }
    override fun addToFavoritePL(list: YTPlayList) { //не используется
    }
}