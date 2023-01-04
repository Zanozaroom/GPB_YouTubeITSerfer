package com.example.otusproject_ermoshina.ui.screen.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterPage(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val listFragment: List<Fragment>) :
    FragmentStateAdapter(fm,lifecycle ) {

    private lateinit var fragment: Fragment

    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            fragment = listFragment[position]
        else if (position == 1)
            fragment = listFragment[position]
        return fragment
    }
}