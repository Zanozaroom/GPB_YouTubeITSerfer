package com.example.otusproject_ermoshina.ui.user_pageview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.databinding.FragmentUserViewpageBinding
import com.example.otusproject_ermoshina.ui.video.UserVideo
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserViewPage : Fragment() {
    private lateinit var binding: FragmentUserViewpageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserViewpageBinding.inflate(inflater, container, false)
       initViewPager()

        return binding.root
    }

    private fun initViewPager() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabsLayout
        val listFragment = listOf(
            UserPlayList.newInstance(),
            UserVideo.newInstance()
        )

        viewPager.adapter = AdapterPage(childFragmentManager, lifecycle,listFragment)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getString(R.string.iconNameTabPlayList)
                }
                1 -> {
                    tab.text = resources.getString(R.string.iconNameTabVideo)
                }
            }
        }.attach()
    }


}