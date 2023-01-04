package com.example.otusproject_ermoshina.ui.screen.videolist

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
import com.example.otusproject_ermoshina.MainGrafDirections
import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.databinding.FragmentVideolistBinding
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.utill.DecoratorParentGrid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YTVideoListFragment: Fragment(),OnClickVideoList {
    private val viewModel: YTVideoListVM by viewModels()
    lateinit var binding: FragmentVideolistBinding
    private lateinit var yTVideoListPaging: YTVideoListPaging
    private var isLoading = true
    private val adapterVideoIdList: AdapterVideoLists by lazy {
        AdapterVideoLists(this)
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
            addItemDecoration(DecoratorParentGrid(adapterVideoIdList.currentList.size,context))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.layoutManager!!.itemCount
                    if (totalItemCount ==
                        adapterLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                        && !isLoading
                    ) {
                        isLoading = true
                        viewModel.loadMore(yTVideoListPaging)
                    }
                }
            })
        }
    }
    private fun stateScreen(state: ViewModelResult<YTVideoListPaging>){
        when(state){
            BaseViewModel.ErrorLoadingViewModel -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.GONE
                binding.gropeError.visibility = View.VISIBLE
            }
            BaseViewModel.LoadingViewModel -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerVideoList.visibility = View.GONE
                binding.gropeError.visibility = View.GONE
            }
            BaseViewModel.NotMoreLoadingViewModel -> isLoading = false
            is BaseViewModel.SuccessViewModel -> {
                adapterVideoIdList.submitList(state.dataViewModelResult.listVideoList)
                yTVideoListPaging = state.dataViewModelResult
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.gropeError.visibility = View.GONE
                isLoading = false
            }
            else -> {}
        }
    }

    override fun onClickImage(idVideo: String) {
        val action = YTVideoListFragmentDirections.actionVideoListToPageOfVideo(idVideo)
        findNavController().navigate(action)
    }

    override fun onClickIconOpenVideo(idVideo: String) {
        onClickImage(idVideo)
    }

    override fun onClickAddVideoToFavorite(idVideo: String) {
        viewModel.addVideoToFavorite(idVideo)
    }

    override fun onClickOpenChannel(idChannel: String) {
        val action = YTVideoListFragmentDirections.actionVideoListToPlayLists(idChannel)
        findNavController().navigate(action)
    }

}
