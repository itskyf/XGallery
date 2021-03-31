package com.team02.xgallery

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.team02.xgallery.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.photos_fragment,
                R.id.search_fragment,
                R.id.library_fragment -> {
                    binding.topAppBar.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.VISIBLE
                }
                else -> {
                    binding.topAppBar.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }

        if (!viewModel.isAvailableToLogIn()) {
            navController.navigate(R.id.login_fragment)
        }

        binding.topAppBar.setOnMenuItemClickListener() { item ->
            when (item.itemId) {
                R.id.ic_add_photo -> {

                    true
                }
                R.id.ic_account -> {

                    true
                }
                else -> false
            }
        }
    }
}