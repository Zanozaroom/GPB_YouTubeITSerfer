package com.example.otusproject_ermoshina.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.domain.model.YTSearchAndTitle
import com.example.otusproject_ermoshina.databinding.FragmentMainBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import com.example.otusproject_ermoshina.utill.DecoratorParentChannels
import com.example.otusproject_ermoshina.ui.base.observeEvent
import dagger.hilt.android.AndroidEntryPoint

interface OnClickYTListener{
    fun onClickOpenChannel(idChannel: String)
    fun onClickOpenMore(action: String)
    fun onClickOnImage(action:String)
}
@AndroidEntryPoint
class FragmentMain: Fragment(), OnClickYTListener {
    private val viewModel: MainViewModel by navGraphViewModels(R.id.mhome) {
        defaultViewModelProviderFactory
    }
    lateinit var binding: FragmentMainBinding
    private val adapterMainSearch: AdapterMainParent by lazy {
        AdapterMainParent( this)
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
    private fun stateScreen(state: ViewModelResult<List<YTSearchAndTitle>>){
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
            else -> {}//иного быть не может
        }
    }



    override fun onClickOpenChannel(idChannel: String) {
        val actionNav = FragmentMainDirections.actionFragmentMainToPlayLists(idChannel)
        findNavController().navigate(actionNav)
    }

    override fun onClickOpenMore(action: String) {
        val actionNav = FragmentMainDirections.actionFragmentMainToSearchFragment(action)
        findNavController().navigate(actionNav)
    }

    override fun onClickOnImage(action: String) {
        val actionNav = FragmentMainDirections.actionFragmentMainToPageOfVideo(action)
        findNavController().navigate(actionNav)
    }


}