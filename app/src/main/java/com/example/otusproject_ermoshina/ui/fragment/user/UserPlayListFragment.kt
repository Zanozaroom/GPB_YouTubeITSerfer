package com.example.otusproject_ermoshina.ui.fragment.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.databinding.FragmentPlaylistBinding
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.ui.fragment.playlist.AdapterPlayList
import com.example.otusproject_ermoshina.ui.base.BasePLFragment
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserPlayListFragment : BasePLFragment() {

    companion object {
        fun newInstance() = UserPlayListFragment()
    }

    private val adapterVideoIdList: AdapterPlayList by lazy {
        AdapterPlayList(this@UserPlayListFragment, this@UserPlayListFragment)
    }

    private val viewModel: UserPlayListViewModel by viewModels()

    override lateinit var binding: FragmentPlaylistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        viewModel.state.observe(viewLifecycleOwner) {
            stateScreen(it)
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
        initAdapterSetting()
        return binding.root
    }

    override fun openPlayList(idPlayList: String) {
        // TODO("Not yet implemented")
    }

    override fun deleteFromFavorite(idPlayList: String) {
        viewModel.deletePlayList(idPlayList)
    }

    private fun initAdapterSetting() {
        binding.recyclerVideoList.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = adapterLayoutManager

        }
    }

    private fun stateScreen(state: ViewModelResult<List<YTPlayList>>) {
        when (state) {
            is ErrorLoadingViewModel -> showError()
            is LoadingViewModel -> showLoading()
            is SuccessViewModel -> {
                adapterVideoIdList.submitList(state.dataViewModelResult)
                showResult()
            }
            else -> {}
        }
    }

    override fun addToFavoritePL(list: YTPlayList) { //не используется
    }
}