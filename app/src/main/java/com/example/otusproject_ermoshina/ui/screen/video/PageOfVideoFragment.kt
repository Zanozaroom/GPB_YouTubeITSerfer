package com.example.otusproject_ermoshina.ui.screen.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.databinding.FragmentPageVideoBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.*
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageOfVideoFragment : Fragment() {
    lateinit var binding: FragmentPageVideoBinding
    private val viewModel: PageOfVideoVM by viewModels()
    private lateinit var ytVideo: YTVideo
    private val args: PageOfVideoFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageVideoBinding.inflate(inflater, container, false)
        lifecycle.addObserver(binding.video)

        binding.video.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                val videoId = args.idVideo
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.screenState.observe(viewLifecycleOwner) {
            stateUI(it)
        }
        binding.titleChannel.setOnClickListener {
            val actionNav = PageOfVideoFragmentDirections.actionPageOfVideoToPlayLists(ytVideo.channelId)
            findNavController().navigate(actionNav)
        }
        binding.addVideoToFavorite.setOnClickListener {
            viewModel.addVideoToFavorite(ytVideo)
        }
        binding.buttonErrorLoad.setOnClickListener {
            viewModel.tryLoad()
        }
        return binding.root
    }

    private fun setUI(data: YTVideo) {
       with(binding){
           titleChannel.text = data.channelTitle
           titleVideo.text = data.title
           textAllLiked.text = data.likeCount.toString()
           textAllWatched.text = data.viewCount.toString()
           descriptionVideo.text = data.description
       }
    }

    private fun stateUI(state: ViewModelResult<YTVideo>) {
        when (state) {
            is ErrorLoadingViewModel -> {
                with(binding) {
                    gropeError.visibility = View.VISIBLE
                    gropeData.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
            is LoadingViewModel -> {
                with(binding) {
                    gropeError.visibility = View.GONE
                    gropeData.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            }
            is SuccessViewModel -> {
                with(binding) {
                    setUI(state.dataViewModelResult)
                    ytVideo = state.dataViewModelResult

                    gropeError.visibility = View.GONE
                    gropeData.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            else -> {}
        }
    }
}

