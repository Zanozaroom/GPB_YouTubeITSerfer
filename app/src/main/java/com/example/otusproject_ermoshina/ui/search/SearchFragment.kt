package com.example.otusproject_ermoshina.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        val recView = binding.recyclerVideoList
        recView.apply {
            recView.adapter = adapterMainSearch
            addItemDecoration(
                DecoratorParentChannels(
                    adapterMainSearch.currentList.size,
                    requireContext()
                )
            )
        }
        recView.layoutManager = LinearLayoutManager(requireContext()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.excLoad()
        }
        return binding.root
    }
    fun stateScreen(state: LoadingResult<List<YTSearch>>){
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
            }
            LoadingResult.Empty -> TODO()
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