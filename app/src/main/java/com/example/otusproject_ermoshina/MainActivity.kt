package com.example.otusproject_ermoshina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.otusproject_ermoshina.databinding.ActivityMainBinding
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.screen.main.FragmentMain
import com.example.otusproject_ermoshina.ui.screen.playlist.YTPlayListFragment
import com.example.otusproject_ermoshina.ui.screen.search.SearchFragment
import com.example.otusproject_ermoshina.ui.screen.user.UserViewPage
import com.example.otusproject_ermoshina.ui.screen.video.PageOfVideoFragment
import com.example.otusproject_ermoshina.ui.screen.videolist.YTVideoListFragment
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ContractNavigator {
    lateinit var binding: ActivityMainBinding
    lateinit var tolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tolbar = binding.myToolbar
        setSupportActionBar(tolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, FragmentMain(), "Main")
                .commit()
        }

        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mhome -> {
                    supportFragmentManager.saveBackStack(USER_BS)
                    supportFragmentManager.restoreBackStack(MAIN_BS)
                    true
                }
                R.id.myvideos -> {
                    supportFragmentManager.saveBackStack(MAIN_BS)
                    supportFragmentManager.restoreBackStack(USER_BS)
                    startFragmentUserStack(UserViewPage())
                    true
                }

                else -> false
            }
        }
    }

    companion object {
        const val MAIN_BS = "main"
        const val USER_BS = "user"
    }

    override fun startYTPlayListFragmentMainStack(idChannel: String) {
        startFragmentMainStack(YTPlayListFragment.newInstance(idChannel))
    }

    override fun startSearchFragmentMainStack(question: String) {
        startFragmentMainStack(SearchFragment.newInstance(question))
    }

    override fun startPageOfVideoFragmentMainStack(idVideo: String) {
        startFragmentMainStack(PageOfVideoFragment.newInstance(idVideo))
    }
    override fun startYTVideoListFragmentMainStack(idPlayList: String){
        startFragmentMainStack(YTVideoListFragment.newInstance(idPlayList))
    }

    override fun startPageOfVideoFragmentUserStack(idVideo: String) {
        startFragmentUserStack(PageOfVideoFragment.newInstance(idVideo))
    }

    override fun startYTVideoListFragmentUserStack(idPlayList: String) {
        startFragmentUserStack(YTVideoListFragment.newInstance(idPlayList))
    }

    override fun startYTPlayListFragmentUserStack(idChannel: String) {
        startFragmentUserStack(YTPlayListFragment.newInstance(idChannel))
    }

    private fun startFragmentMainStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(MAIN_BS)
            .replace(R.id.fragment_container, fragment)
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .commit()
    }

    private fun startFragmentUserStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(USER_BS)
            .replace(R.id.fragment_container, fragment)
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .commit()
    }

    override fun setActionBarNavigateBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun removeActionBarNavigateBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun setTitle(title: String?) {
        supportActionBar?.title = title ?: getString(R.string.mainAppTitle)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}