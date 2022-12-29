package com.example.otusproject_ermoshina.ui.fragment.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.databinding.FragmentPlaylistBinding
import com.example.otusproject_ermoshina.domain.model.YTChannelAndPlayList
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.ui.base.BasePLFragment
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YTPlayListFragment : BasePLFragment() {
    private val viewModel: YTPlayListsVM by viewModels()
    override lateinit var binding: FragmentPlaylistBinding
    private lateinit var ytChannelAndPlayList: YTChannelAndPlayList
    private var isLoading = true
    private val adapterVideoIdList: AdapterPlayList by lazy {
        AdapterPlayList(this@YTPlayListFragment, this@YTPlayListFragment)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        viewModel.toastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        initAdapterSetting()
        viewModel.screenState.observe(viewLifecycleOwner) {
            stateScreen(it)
        }
        return binding.root
    }

    override fun openPlayList(idPlayList: String) {
        val actionNav = YTPlayListFragmentDirections.actionPlayListsToVideoList(idPlayList)
        findNavController().navigate(actionNav)
    }

    override fun deleteFromFavorite(idPlayList: String) {
        //
    }

    override fun addToFavoritePL(list: YTPlayList) {
      viewModel.addToFavoritePL(list)
    }

    private fun initAdapterSetting() {
        binding.recyclerVideoList.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = adapterLayoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.layoutManager!!.itemCount
                    if (totalItemCount ==
                        adapterLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                        && !isLoading
                    ) {
                        viewModel.loadMore(ytChannelAndPlayList)
                        isLoading = true
                    }
                }
            })
        }
    }

    private fun stateScreen(state: ViewModelResult<YTChannelAndPlayList>) {
        when (state) {
            is EmptyResultViewModel -> showEmpty()
            is ErrorLoadingViewModel -> showError()
            is LoadingViewModel -> showLoading()
            is NotMoreLoadingViewModel -> isLoading = false
            is SuccessViewModel -> {
                ytChannelAndPlayList = state.dataViewModelResult
                adapterVideoIdList.submitList(ytChannelAndPlayList.listVideos)
                showResult()
                isLoading = false
            }
        }
    }

}
