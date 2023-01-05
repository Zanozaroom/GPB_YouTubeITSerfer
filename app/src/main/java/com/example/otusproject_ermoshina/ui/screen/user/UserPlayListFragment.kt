package com.example.otusproject_ermoshina.ui.screen.user

import android.content.Context
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
import com.example.otusproject_ermoshina.ui.base.BasePLFragment
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import com.example.otusproject_ermoshina.ui.base.navigator
import com.example.otusproject_ermoshina.ui.screen.playlist.YTPlayListFragment
import com.example.otusproject_ermoshina.ui.screen.videolist.YTVideoListFragment
import com.example.otusproject_ermoshina.utill.DecoratorParentGrid
import dagger.hilt.android.AndroidEntryPoint

interface ActionUserPlayList {
    fun openPlayList(idPlayList: String)
    fun deleteFromFavoritePL(idPlayList: String)
    fun openChannel(idChannel: String)
}

@AndroidEntryPoint
class UserPlayListFragment : BasePLFragment(),ActionUserPlayList {

    companion object {
        fun newInstance() = UserPlayListFragment()
    }

    private val adapterVideoIdList: AdapterUserPlayList by lazy {
        AdapterUserPlayList(this)
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
       this.navigator().startFragmentUserStack(YTVideoListFragment.newInstance(idPlayList))
    }

    override fun deleteFromFavoritePL(idPlayList: String) {
        viewModel.deletePlayList(idPlayList)
    }

    override fun openChannel(idChannel: String) {
        this.navigator().startFragmentUserStack(YTPlayListFragment.newInstance(idChannel))
    }

    private fun initAdapterSetting() {
        binding.recyclerVideoList.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(DecoratorParentGrid(adapterVideoIdList.currentList.size,context))
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
}