package com.example.otusproject_ermoshina.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.databinding.FragmentPageVideoBinding
import com.example.otusproject_ermoshina.ui.base.BaseViewModel.LoadingResult
import com.example.otusproject_ermoshina.ui.base.observeEvent
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageOfVideo : Fragment() {
    lateinit var binding: FragmentPageVideoBinding
    private val viewModel: PageOfVideoVM by viewModels()
    private val args: PageOfVideoArgs by navArgs()
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
        return binding.root
    }

    private fun setUI(data:YTVideo) {
       with(binding){
           titleChannel.text = data.channelTitle
           titleVideo.text = data.title
           textAllLiked.text = data.likeCount.toString()
           textAllWatched.text = data.viewCount.toString()
           descriptionVideo.text = data.description
       }
    }

    private fun stateUI(state: LoadingResult<YTVideo>) {
        when (state) {
            is LoadingResult.Error -> {
                with(binding) {
                    gropeError.visibility = View.VISIBLE
                    gropeData.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
            is LoadingResult.Loading -> {
                with(binding) {
                    gropeError.visibility = View.GONE
                    gropeData.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            }
            is LoadingResult.Success -> {
                with(binding) {
                    setUI(state.dataList)
                    gropeError.visibility = View.GONE
                    gropeData.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            LoadingResult.Empty -> TODO()
        }
    }
}

