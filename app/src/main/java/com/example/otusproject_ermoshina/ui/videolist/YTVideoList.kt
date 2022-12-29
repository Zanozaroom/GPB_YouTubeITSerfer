package com.example.otusproject_ermoshina.ui.videolist

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
import com.example.otusproject_ermoshina.base.YTVideoList
import com.example.otusproject_ermoshina.databinding.FragmentVideolistBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.LoadingResult
import com.example.otusproject_ermoshina.ui.base.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YTVideoList: Fragment() {
    private val viewModel: YTVideoListVM by viewModels()
    lateinit var binding: FragmentVideolistBinding
    private var isLoading = true
    private val adapterVideoIdList: AdapterVideoLists by lazy {
        AdapterVideoLists{
           val action = YTVideoListDirections.actionVideoListToPageOfVideo(it)
            findNavController().navigate(action)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideolistBinding.inflate(inflater, container, false)

        viewModel.toastEvent.observeEvent(this){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.screenState.observe(viewLifecycleOwner){
            stateScreen(it)
        }

        initAdapterSetting()
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.tryLoad()
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
                        && !isLoading
                    ) {
                        viewModel.loadMore()
                    }
                }
            })
        }
    }
    private fun stateScreen(state: LoadingResult<List<YTVideoList>>){
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
            is LoadingResult.Success<List<YTVideoList>> -> {
                adapterVideoIdList.submitList(state.dataList)
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
                isLoading = false
            }
            LoadingResult.Empty -> {}//не испозьуем
            LoadingResult.LoadingMore -> isLoading = true
        }
    }


}
