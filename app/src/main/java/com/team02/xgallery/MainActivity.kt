package com.team02.xgallery

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.team02.xgallery.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    // ----- Add photos -----
    private val newMediaURIs: ArrayList<Uri> = ArrayList()
    val getContent =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { mediaURIs: List<Uri> ->
            // Handle the returned Uris
            newMediaURIs.clear()
            for (uri in mediaURIs) {
                newMediaURIs.add(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        // ----- Show/Hide top app bar & bottom nav -----
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

        // ----- Check Login State -----
        if (!viewModel.isAvailableToLogIn()) {
            navController.navigate(R.id.login_fragment)
        }

        // ----- External Storage Permission -----
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                getContent.launch("image/*")
            } else {
                Snackbar.make(
                    binding.root,
                    "Please accept to upload photos.",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("OK") {
                        // TODO(): Navigate to the Setting Fragment
                        // navController.navigate(R.id.setting_fragment)
                    }
                    .show()
            }
        }

        // ----- On Click -----
        binding.topAppBar.setOnMenuItemClickListener() { item ->
            when (item.itemId) {
                R.id.ic_add_photo -> {
                    requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
                    true
                }
                R.id.ic_account -> {
                    // TODO(): Navigate to the Account Fragment
                    // navController.navigate(R.id.account_fragment)
                    true
                }
                else -> false
            }
        }
    }
}