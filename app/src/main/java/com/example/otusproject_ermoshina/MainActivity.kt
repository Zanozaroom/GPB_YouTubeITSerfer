package com.example.otusproject_ermoshina

import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.otusproject_ermoshina.databinding.ActivityMainBinding
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import com.example.otusproject_ermoshina.ui.screen.main.FragmentMain
import com.example.otusproject_ermoshina.ui.screen.user.UserViewPage
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
        /*setBackPressedCallback()*/
    }

/*    private fun setBackPressedCallback() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("AAA", "Activity back pressed invoked")
                    // Do custom work here

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }*/

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
                    Log.i("AAA", " R.id.myvideos")
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

    override fun startFragmentMainStack(fragment: Fragment) {
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

    override fun startFragmentUserStack(fragment: Fragment) {
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
        Log.i("AAA", "requireActivity().actionBar ${tolbar}")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun removeActionBarNavigateBack() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}