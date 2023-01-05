package com.example.otusproject_ermoshina.ui.screen.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.databinding.FragmentUserViewpageBinding
import com.example.otusproject_ermoshina.ui.base.navigator
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserViewPage : Fragment() {
    private lateinit var binding: FragmentUserViewpageBinding

    override fun onStart() {
        super.onStart()
        this.navigator().removeActionBarNavigateBack()

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                        val backstack = childFragmentManager.backStackEntryCount
                    if(backstack==0) requireActivity().finish()
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher
                    }
                }
            }
        )
    }

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
            UserPlayListFragment.newInstance(),
            UserVideoFragment.newInstance()
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
companion object{
    fun newInstance():UserViewPage {
        val args = Bundle()

        val fragment = UserViewPage()
        fragment.arguments = args
        return fragment
    }
}

}