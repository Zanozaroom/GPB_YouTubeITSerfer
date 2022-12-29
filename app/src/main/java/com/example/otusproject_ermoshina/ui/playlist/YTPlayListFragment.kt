package com.example.otusproject_ermoshina.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.databinding.FragmentPlaylistBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.LoadingResult
import com.example.otusproject_ermoshina.ui.base.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YTPlayListFragment : BasePLFragment() {
    private val viewModel: YTPlayListsVM by viewModels()
    override lateinit var binding: FragmentPlaylistBinding
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

    override fun openPL(idList: String) {
        val actionNav = YTPlayListFragmentDirections.actionPlayListsToVideoList(idList)
        findNavController().navigate(actionNav)
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
                        viewModel.loadMore()
                    }
                }
            })
        }
    }

    private fun stateScreen(state: LoadingResult<List<YTPlayList>>) {
        when (state) {
            is LoadingResult.Error -> {
                showError()
            }
            is LoadingResult.Loading -> {
                showLoading()
            }
            is LoadingResult.Success -> {
                adapterVideoIdList.submitList(state.dataList)
                isLoading = false
                showResult()
            }
            LoadingResult.Empty -> {
                showEmpty()
            }
            LoadingResult.LoadingMore -> isLoading = true
        }
    }

    override fun removeFavorite(list: YTPlayList) {
    }
}
