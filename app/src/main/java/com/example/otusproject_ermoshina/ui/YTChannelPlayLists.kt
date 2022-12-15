package com.example.otusproject_ermoshina.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.databinding.FragmentYtChannelPlaylistsBinding
import com.example.otusproject_ermoshina.ui.viewmodels.YTListIdViewModel
import com.example.otusproject_ermoshina.utill.SwipeToFavoriteList
import com.example.otusproject_ermoshina.utill.YTAdapterVideoIdList
import com.example.otusproject_ermoshina.utill.base.Move

class YTChannelPlayLists : Fragment() {
    private val args: YTChannelPlayListsArgs by navArgs()
    private val viewModel: YTListIdViewModel by viewModels { YTListIdViewModel.Factory }
    lateinit var binding: FragmentYtChannelPlaylistsBinding
    private val adapterVideoIdList: YTAdapterVideoIdList by lazy {
        YTAdapterVideoIdList() {
            Toast.makeText(requireContext(), "Click $it", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedInstanceState == null){
            viewModel.loadPlayLists(args.idChannel)
        }
        binding = FragmentYtChannelPlaylistsBinding.inflate(inflater, container, false)
      viewModel.listYouTube.observe(viewLifecycleOwner) {
          adapterVideoIdList.submitList(it)
           }
        val recView = binding.rw

        initAdapterSetting()
        initSwipeCallBack()
        recView.setOnClickListener {
            Toast.makeText(requireContext(), "AAA", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
    private fun initAdapterSetting(){
        binding.rw.apply {
            adapter = adapterVideoIdList
            val adapterLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = adapterLayoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = recyclerView.layoutManager!!.itemCount
                    if (totalItemCount ==
                        adapterLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                    ) {
                        val idChannel = args.idChannel
                        val token = adapterVideoIdList.currentList[0].nextToken
                        viewModel.loadPlayLists(idChannel,token)
                    }
                }
            })
        }
    }
    private fun initSwipeCallBack(){
        val  swipeCallBack = SwipeToFavoriteList(requireContext()){ move, position1, _ ->
            when(move) {
                //удаляем
                Move.START -> {
                    //в этом фрагменте не используем
                }
                //добавляем в избранное
                Move.END -> {
                   /* viewModel.addToListFavoriteArt(adapterListArt.currentList[position1])*/
                    adapterVideoIdList.notifyItemChanged(position1)
                    Toast.makeText(requireContext(), "R.string.toast_add_art", Toast.LENGTH_SHORT).show()

                }
                Move.MOVED ->{
                    //в этом фрагменте не используем
                }
            }
        }
        val itemCallBack = ItemTouchHelper(swipeCallBack)
        itemCallBack.attachToRecyclerView(binding.rw)
    }
}
