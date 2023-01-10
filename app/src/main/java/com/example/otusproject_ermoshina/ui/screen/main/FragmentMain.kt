package com.example.otusproject_ermoshina.ui.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.model.YTMainFragmentData
import com.example.otusproject_ermoshina.databinding.FragmentMainBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.utill.DecoratorParent
import com.example.otusproject_ermoshina.ui.base.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface OnClickYTListener{
    fun onClickOpenChannel(idChannel: String)
    fun onClickOpenMore(question: String, title: String)
    fun onClickOnImage(idVideo:String)
}
@AndroidEntryPoint
class FragmentMain: Fragment(), OnClickYTListener {

    @Inject
    lateinit var navigator: ContractNavigator

    private val viewModel: MainViewModel by viewModels()
    lateinit var binding: FragmentMainBinding
    private val adapterMainSearch: AdapterMainParent by lazy {
        AdapterMainParent( this)
    }


    override fun onStart() {
        super.onStart()
        navigator.removeActionBarNavigateBack()
        navigator.setTitle(getString(R.string.mainAppTitle))
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
            Toast.makeText(requireContext(), it,Toast.LENGTH_SHORT).show()}
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.loadData()
        }

        setupRecyclerView()

        return binding.root
        }

    override fun onClickOpenChannel(idChannel: String) {
        navigator.startYTPlayListFragmentMainStack(idChannel)
    }

    override fun onClickOpenMore(question: String, title: String) {
        navigator.startSearchFragmentMainStack(question, title)
    }

    override fun onClickOnImage(idVideo: String) {
        navigator.startPageOfVideoFragmentMainStack(idVideo)
    }

    private fun setupRecyclerView() {
        val recView = binding.recyclerVideoList
        recView.apply {
            recView.adapter = adapterMainSearch
            addItemDecoration(DecoratorParent(adapterMainSearch.currentList.size,requireContext()))
        }

        recView.layoutManager = LinearLayoutManager(requireContext()).apply {
            this.orientation = LinearLayoutManager.VERTICAL}
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
            BaseViewModel.EmptyResultViewModel -> {}
            BaseViewModel.NotMoreLoadingViewModel -> {}
        }
    }

}