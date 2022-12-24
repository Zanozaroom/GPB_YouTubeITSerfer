package com.example.otusproject_ermoshina.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.databinding.FragmentYtChannelPlaylistsBinding
import com.example.otusproject_ermoshina.utill.AdapterVideoListByChannel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.LoadingResult
import com.example.otusproject_ermoshina.ui.base.observeEvent
import dagger.hilt.android.AndroidEntryPoint

interface AddtoFavoritePlayList{
    fun addToFavorite(list: YTPlayListOfChannel)
}
interface OpenPlayList{
    fun openPlayList(idList: String)
}

@AndroidEntryPoint
class PlayLists : Fragment(), AddtoFavoritePlayList, OpenPlayList {
    private val viewModel: PlayListsVM by viewModels ()
    lateinit var binding: FragmentYtChannelPlaylistsBinding
    private val adapterVideoIdList: AdapterVideoListByChannel by lazy {
        AdapterVideoListByChannel(this, this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      binding = FragmentYtChannelPlaylistsBinding.inflate(inflater, container, false)

        viewModel.toastEvent.observeEvent(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        initAdapterSetting()
        viewModel.screenState.observe(viewLifecycleOwner){
            stateScreen(it)
        }

        return binding.root
    }
    private fun initAdapterSetting(){
        binding.recyclerVideoList.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = adapterLayoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.layoutManager!!.itemCount
                    if (totalItemCount ==
                        adapterLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                    ) {
                        viewModel.loadMore()
                    }
                }
            })
        }
    }


    override fun addToFavorite(list: YTPlayListOfChannel) {
        viewModel.addToFavoritePL(list)
    }

    override fun openPlayList(idList: String) {
        val actionNav = PlayListsDirections.actionPlayListsToVideoList(idList)
        findNavController().navigate(actionNav)
    }
    private fun stateScreen(state: LoadingResult<List<YTPlayListOfChannel>>){
        when(state){
            is LoadingResult.Error -> {
                binding.gropeError.visibility = View.VISIBLE
                binding.gropeResult.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.imageNotData.visibility = View.GONE

            }
            is LoadingResult.Loading -> {
                binding.gropeError.visibility = View.GONE
                binding.gropeResult.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.imageNotData.visibility = View.GONE
            }
            is LoadingResult.Success -> {
                adapterVideoIdList.submitList(state.dataList)
                binding.gropeError.visibility = View.GONE
                binding.gropeResult.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.imageNotData.visibility = View.GONE
            }
            LoadingResult.Empty -> {
                binding.gropeError.visibility = View.GONE
                binding.gropeResult.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.imageNotData.visibility = View.VISIBLE
            }
        }
    }



}
