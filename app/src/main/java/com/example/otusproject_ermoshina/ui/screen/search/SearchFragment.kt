package com.example.otusproject_ermoshina.ui.screen.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.databinding.FragmentResultSearchBinding
import com.example.otusproject_ermoshina.domain.model.YTSearchPaging
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import com.example.otusproject_ermoshina.ui.base.navigator
import com.example.otusproject_ermoshina.ui.screen.video.PageOfVideoFragment
import com.example.otusproject_ermoshina.utill.DecoratorParent
import dagger.hilt.android.AndroidEntryPoint

interface OnClickSearch{
    fun openChannel(idChannel:String)
    fun openVideo(idVideo:String)
}

@AndroidEntryPoint
class SearchFragment: Fragment(), OnClickSearch {
     private val viewModel: SearchFragmentVM by viewModels()
    lateinit var binding: FragmentResultSearchBinding
    lateinit var ytSearchPaging: YTSearchPaging
    private var isLoading = true
    private val adapterMainSearch: AdapterSearch by lazy {
        AdapterSearch( this)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.navigator().setActionBarNavigateBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultSearchBinding.inflate(inflater, container, false)
        viewModel.state.observe (viewLifecycleOwner) {
            stateScreen(it)
        }
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.load()
        }
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerVideoList.apply {
            val adapterLayoutManager = LinearLayoutManager(requireContext())
            adapterLayoutManager.orientation = LinearLayoutManager.VERTICAL
            adapter = adapterMainSearch
            addItemDecoration(
                DecoratorParent(
                    adapterMainSearch.currentList.size,
                    requireContext()
                )
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = adapterLayoutManager.itemCount
                    if (totalItemCount ==
                        adapterLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                        && !isLoading
                    ) {
                        Log.i("EEE", "addOnScrollListener сработал")
                        viewModel.loadMore(ytSearchPaging)
                        isLoading = true
                    }
                }
            })
        }
    }

    private fun stateScreen(state: ViewModelResult<YTSearchPaging>){
        when(state){
            is ErrorLoadingViewModel -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.VISIBLE
                binding.messageErrorLoad.visibility = View.VISIBLE
            }
            is LoadingViewModel -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerVideoList.visibility = View.GONE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
            }
            is SuccessViewModel -> {
                adapterMainSearch.submitList(state.dataViewModelResult.listSearchVideo)
                ytSearchPaging = state.dataViewModelResult
                binding.progressBar.visibility = View.GONE
                binding.recyclerVideoList.visibility = View.VISIBLE
                binding.buttonErrorLoad.visibility = View.GONE
                binding.messageErrorLoad.visibility = View.GONE
                isLoading = false
            }
            is NotMoreLoadingViewModel -> isLoading = false
            else -> {}
        }
    }


    override fun openChannel(idChannel: String) {
        this.navigator().startFragmentMainStack(PageOfVideoFragment.newInstance(idChannel))
    }

    override fun openVideo(idVideo: String) {
        this.navigator().startFragmentMainStack(PageOfVideoFragment.newInstance(idVideo))
    }

    companion object{
        const val ARGS_QUESTION = "question"

        fun newInstance(question: String):SearchFragment {
            val args = Bundle()
            args.putString(ARGS_QUESTION, question)
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onStart() {
        super.onStart()
        Log.i("AAA", "onStop SearchFragment")
    }
    override fun onStop() {
        super.onStop()
        Log.i("AAA", "onStop SearchFragment")
    }

    override fun onPause() {
        super.onPause()
        super.onStop()
        Log.i("AAA", "onStop SearchFragment")
    }
}