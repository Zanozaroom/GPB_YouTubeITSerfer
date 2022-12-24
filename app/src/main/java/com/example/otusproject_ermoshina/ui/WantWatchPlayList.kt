package com.example.otusproject_ermoshina.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.otusproject_ermoshina.databinding.FragmentYtWantWatchPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WantWatchPlayList : Fragment() {
    private lateinit var viewModel: WantWatchPlayListViewModel
    private lateinit var binding: FragmentYtWantWatchPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYtWantWatchPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }



}