package com.example.otusproject_ermoshina.ui.screen.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.databinding.FragmentUserVideoBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.utill.DecoratorParentGrid
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface UserVideoAction {
    fun openVideo(idVideo: String)
    fun deleteVideo(video: YTVideo)
    fun openChannel(idChannel: String)
}

@AndroidEntryPoint
class UserVideoFragment : Fragment(), UserVideoAction {
    @Inject
    lateinit var navigator: ContractNavigator
    private val viewModel: UserVideoViewModel by viewModels()
    lateinit var binding: FragmentUserVideoBinding
    private val adapterVideoUser: UserVideoAdapter by lazy {
        UserVideoAdapter(this)
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
            addItemDecoration(DecoratorParentGrid(adapterVideoUser.currentList.size, context))
        }
    }

    override fun openVideo(idVideo: String) {
        navigator.startPageOfVideoFragmentUserStack(idVideo)
    }

    override fun deleteVideo(video: YTVideo) {
        viewModel.deleteVideo(video)
    }

    override fun openChannel(idChannel: String) {
        navigator.startYTPlayListFragmentUserStack(idChannel)
    }

    private fun stateScreen(state: ViewModelResult<List<YTVideo>>) {
        when (state) {
            is BaseViewModel.ErrorLoadingViewModel -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.VISIBLE
                binding.messageErrorLoad.visibility = View.VISIBLE
                binding.emptyResultImage.visibility = View.GONE
            }
            is BaseViewModel.LoadingViewModel -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
                binding.emptyResultImage.visibility = View.GONE
            }
            is BaseViewModel.SuccessViewModel -> {
                adapterVideoUser.submitList(state.dataViewModelResult)
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
                binding.emptyResultImage.visibility = View.GONE
            }
            BaseViewModel.EmptyResultViewModel -> {
                binding.progressBar.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.GONE
                binding.emptyResultImage.visibility = View.VISIBLE

            }
            BaseViewModel.NotMoreLoadingViewModel -> {}//нет действия
        }
    }


    companion object {
        fun newInstance() = UserVideoFragment()
    }

}