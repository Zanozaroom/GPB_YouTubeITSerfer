package com.example.otusproject_ermoshina.ui.screen.videolist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.databinding.FragmentVideolistBinding
import com.example.otusproject_ermoshina.domain.model.YTVideoListPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.ViewModelResult
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.example.otusproject_ermoshina.utill.DecoratorParentGrid
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YTVideoListFragment : Fragment(), OnClickVideoList {
    @Inject
    lateinit var navigator: ContractNavigator
    private val viewModel: YTVideoListVM by viewModels()
    lateinit var binding: FragmentVideolistBinding
    private lateinit var yTVideoListPaging: YTVideoListPaging
    private var isLoading = true
    private val adapterVideoIdList: AdapterVideoLists by lazy {
        AdapterVideoLists(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator.setActionBarNavigateBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVideolistBinding.inflate(inflater, container, false)

        viewModel.toastEvent.observeEvent(this) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()}
        viewModel.screenState.observe(viewLifecycleOwner) {
            stateScreen(it)}
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.tryLoad()}

        initAdapterSetting()
        return binding.root
    }

    override fun onClickImage(idVideo: String) {
        navigator.startPageOfVideoFragmentMainStack(idVideo)
    }

    override fun onClickIconOpenVideo(idVideo: String) {
        onClickImage(idVideo)
    }

    override fun onClickAddVideoToFavorite(idVideo: String) {
        viewModel.addVideoToFavorite(idVideo)
    }

    override fun onClickOpenChannel(idChannel: String) {
        navigator.startYTPlayListFragmentMainStack(idChannel)
    }

    private fun initAdapterSetting() {
        binding.recyclerVideoList.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = adapterLayoutManager
            addItemDecoration(DecoratorParentGrid(adapterVideoIdList.currentList.size, context))

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

    private fun stateScreen(state: ViewModelResult<YTVideoListPaging>) {
        when (state) {
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
                navigator.setTitle(yTVideoListPaging.listVideoList.first().channelTitle)
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.gropeError.visibility = View.GONE
                isLoading = false
            }
            else -> {}
        }
    }


    companion object {
        const val ARGS_VIDEO_LIST_ID = "idVideoList"

        fun newInstance(idVideoList: String): YTVideoListFragment {
            val args = Bundle()
            args.putString(ARGS_VIDEO_LIST_ID, idVideoList)
            val fragment = YTVideoListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
