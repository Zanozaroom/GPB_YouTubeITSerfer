package com.example.otusproject_ermoshina.ui.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.databinding.FragmentUserVideoBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.ui.videolist.AdapterUserVideo
import com.example.otusproject_ermoshina.ui.videolist.UserVideoViewModel
import dagger.hilt.android.AndroidEntryPoint

interface UserVideoAction {
    fun openVideo(idVideo: String)
    fun deleteVideo(video: YTVideo)
}

@AndroidEntryPoint
class UserVideo : Fragment(), UserVideoAction {
    private val viewModel: UserVideoViewModel by viewModels()
    lateinit var binding: FragmentUserVideoBinding
    private val adapterVideoUser: AdapterUserVideo by lazy {
        AdapterUserVideo(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserVideoBinding.inflate(inflater, container, false)
        viewModel.toastEvent.observeEvent(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.state.observe(viewLifecycleOwner) {
            stateScreen(it)
        }
        initAdapterSetting()
        return binding.root
    }

    private fun initAdapterSetting() {
        binding.recyclerVideoList.apply {
            adapter = adapterVideoUser
            val adapterLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = adapterLayoutManager
        }
    }

    private fun stateScreen(state: BaseViewModel.LoadingResult<List<YTVideo>>) {
        when (state) {
            is BaseViewModel.LoadingResult.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.VISIBLE
                binding.messageErrorLoad.visibility = View.VISIBLE
            }
            is BaseViewModel.LoadingResult.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
            }
            is BaseViewModel.LoadingResult.Success<List<YTVideo>> -> {
                adapterVideoUser.submitList(state.dataList)
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE

            }

            else -> {}
        }
    }
    override fun openVideo(idVideo: String) {
      //  TODO("Not yet implemented")
    }

    override fun deleteVideo(video: YTVideo) {
     viewModel.deleteVideo(video)
    }

    companion object {
        fun newInstance() = UserVideo()
    }
}