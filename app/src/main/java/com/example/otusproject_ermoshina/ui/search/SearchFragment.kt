package com.example.otusproject_ermoshina.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.base.YTSearch
import com.example.otusproject_ermoshina.databinding.FragmentResultSearchBinding
import com.example.otusproject_ermoshina.utill.DecoratorParentChannels
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.LoadingResult
import dagger.hilt.android.AndroidEntryPoint

interface OnClickSearch{
    fun openChannel(idChannel:String)
    fun openVideo(idVideo:String)
}

@AndroidEntryPoint
class SearchFragment: Fragment(), OnClickSearch {
     private val viewModel: SearchFragmentVM by viewModels()
    lateinit var binding: FragmentResultSearchBinding
    private var isLoading = true
    private val adapterMainSearch: AdapterSearch by lazy {
        AdapterSearch( this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultSearchBinding.inflate(inflater, container, false)
        viewModel.state.observe (viewLifecycleOwner) {
            stateScreen(it)
        }
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.load()
        }
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerVideoList.apply {
            val adapterLayoutManager = LinearLayoutManager(requireContext())
            adapterLayoutManager.orientation = LinearLayoutManager.VERTICAL
            adapter = adapterMainSearch
            addItemDecoration(
                DecoratorParentChannels(
                    adapterMainSearch.currentList.size,
                    requireContext()
                )
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = adapterLayoutManager.itemCount
                    if (totalItemCount ==
                        adapterLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                        && !isLoading
                    ) {
                        Log.i("EEE", "addOnScrollListener сработал")
                        viewModel.loadMore()
                    }
                }
            })
        }
    }

    private fun stateScreen(state: LoadingResult<List<YTSearch>>){
        when(state){
            is LoadingResult.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.VISIBLE
                binding.messageErrorLoad.visibility = View.VISIBLE
            }
            is LoadingResult.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
            }
            is LoadingResult.Success -> {
                adapterMainSearch.submitList(state.dataList)
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
                isLoading = false
            }
            LoadingResult.Empty -> {}//не используется
            LoadingResult.LoadingMore -> isLoading = true
        }
    }


    override fun openChannel(idChannel: String) {
       val action = SearchFragmentDirections.actionSearchFragmentToPlayLists(idChannel)
        findNavController().navigate(action)
    }

    override fun openVideo(idVideo: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToPageOfVideo(idVideo)
        findNavController().navigate(action)
    }
}