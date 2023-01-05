package com.example.otusproject_ermoshina.ui.screen.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otusproject_ermoshina.domain.model.YTMainFragmentData
import com.example.otusproject_ermoshina.databinding.FragmentMainBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import com.example.otusproject_ermoshina.ui.base.navigator
import com.example.otusproject_ermoshina.utill.DecoratorParent
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.ui.screen.playlist.YTPlayListFragment
import com.example.otusproject_ermoshina.ui.screen.search.SearchFragment
import com.example.otusproject_ermoshina.ui.screen.video.PageOfVideoFragment
import dagger.hilt.android.AndroidEntryPoint

interface OnClickYTListener{
    fun onClickOpenChannel(idChannel: String)
    fun onClickOpenMore(action: String)
    fun onClickOnImage(action:String)
}
@AndroidEntryPoint
class FragmentMain: Fragment(), OnClickYTListener {

    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: FragmentMainBinding
    private val adapterMainSearch: AdapterMainParent by lazy {
        AdapterMainParent( this)
    }

    override fun onStart() {
        super.onStart()
        this.navigator().removeActionBarNavigateBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.state.observe (viewLifecycleOwner) {
            stateScreen(it)
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner){
            Toast.makeText(requireContext(), it,Toast.LENGTH_SHORT).show()
        }
        val recView = binding.recyclerVideoList
        recView.apply {
            recView.adapter = adapterMainSearch
            addItemDecoration(
                DecoratorParent(
                    adapterMainSearch.currentList.size,
                    requireContext()
                )
            )
        }
        recView.layoutManager = LinearLayoutManager(requireContext()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.loadData()
        }
        return binding.root
    }

    private fun stateScreen(state: ViewModelResult<List<YTMainFragmentData>>){
        when(state){
            is BaseViewModel.LoadingViewModel -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerVideoList.visibility = View.GONE
                binding.gropeError.visibility = View.GONE
            }
            is BaseViewModel.ErrorLoadingViewModel -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.GONE
                binding.gropeError.visibility = View.VISIBLE
            }
            is BaseViewModel.SuccessViewModel -> {
                adapterMainSearch.submitList(state.dataViewModelResult)
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.gropeError.visibility = View.GONE
            }
            BaseViewModel.EmptyResultViewModel -> TODO()
            BaseViewModel.NotMoreLoadingViewModel -> TODO()
        }
    }

    override fun onClickOpenChannel(idChannel: String) {
        this.navigator().startFragmentMainStack(YTPlayListFragment.newInstance(idChannel))
    }

    override fun onClickOpenMore(action: String) {
        this.navigator().startFragmentMainStack(SearchFragment.newInstance(action))
    }

    override fun onClickOnImage(action: String) {
        this.navigator().startFragmentMainStack(PageOfVideoFragment.newInstance(action))
    }

}