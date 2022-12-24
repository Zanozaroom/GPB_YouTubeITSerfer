package com.example.otusproject_ermoshina

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.otusproject_ermoshina.databinding.ActivityMainBinding
import com.example.otusproject_ermoshina.sources.repositories.RepositoryYouTube
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //нахожу хост графа навигации
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //устанавливаю основной контроллер
        navController = navHost.navController

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.mhome, R.id.myvideos
        ))

        setSupportActionBar(findViewById(R.id.my_toolbar))
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        binding.myToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //установка нижнего тулбар-меню
        binding.bottomNav.setupWithNavController(navController)


    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}