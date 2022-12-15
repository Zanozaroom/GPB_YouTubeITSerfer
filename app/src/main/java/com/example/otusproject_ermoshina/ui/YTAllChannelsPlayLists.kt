package com.example.otusproject_ermoshina.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otusproject_ermoshina.databinding.FragmentYtChannelsListBinding
import com.example.otusproject_ermoshina.ui.viewmodels.ChannelsListScreenState
import com.example.otusproject_ermoshina.ui.viewmodels.YTChannelsListViewModel
import com.example.otusproject_ermoshina.utill.DecoratorParentChannels
import com.example.otusproject_ermoshina.utill.YTAdapterChannelsParent


interface OnClickButtonsChannel{
    fun onClickFindLikeThisChannel(idChannel: String)
    fun onClickOpenAllPlayListsChannel(idChannel: String)
    fun onClickOnImage(idPlayList:String)
}

class YTAllChannelsPlayLists : Fragment(), OnClickButtonsChannel {

    private val viewModel: YTChannelsListViewModel by viewModels { YTChannelsListViewModel.Factory }
    lateinit var binding: FragmentYtChannelsListBinding
    private val adapterChannel: YTAdapterChannelsParent by lazy {
        YTAdapterChannelsParent( this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYtChannelsListBinding.inflate(inflater, container, false)
        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it){
                is ChannelsListScreenState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerChannelsKotlin.visibility = View.GONE
                    binding.buttonErrorLoad.visibility = View.VISIBLE
                    binding.messageErrorLoad.visibility = View.VISIBLE
                }
                is ChannelsListScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerChannelsKotlin.visibility = View.GONE
                    binding.buttonErrorLoad.visibility = View.GONE
                    binding.messageErrorLoad.visibility = View.GONE
                }
                is ChannelsListScreenState.Success -> {
                    adapterChannel.submitList(it.data)
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerChannelsKotlin.visibility = View.VISIBLE
                }
            }

        }
        val recView = binding.recyclerChannelsKotlin
        recView.apply {
            recView.adapter = adapterChannel
            addItemDecoration(DecoratorParentChannels(adapterChannel.currentList.size, requireContext()))
        }
        recView.layoutManager = LinearLayoutManager(requireContext()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.loadStart()
        }
        return binding.root
    }

    override fun onClickFindLikeThisChannel(idChannel: String) {
        Toast.makeText(requireContext(), "AAA $idChannel", Toast.LENGTH_SHORT).show()
    }

    override fun onClickOpenAllPlayListsChannel(idChannel: String) {
        val action = YTAllChannelsPlayListsDirections.actionYTChannelsListToYTChannelPlayLists(idChannel)
        findNavController().navigate(action)
        Toast.makeText(requireContext(), "AAA $idChannel", Toast.LENGTH_SHORT).show()
    }

    override fun onClickOnImage(idPlayList: String) {
        Toast.makeText(requireContext(), "AAA $idPlayList", Toast.LENGTH_SHORT).show()
    }


}