package com.team02.xgallery

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.databinding.ActivityMainBinding
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_XGallery)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        // ----- Show/Hide top app bar & bottom nav -----
        val mainParams = binding.navHostFragment.layoutParams as CoordinatorLayout.LayoutParams
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.collectionsFragment -> {
                    binding.topAppBar.visibility = View.VISIBLE
                    binding.bottomNav.visibility = View.VISIBLE
                    mainParams.behavior = AppBarLayout.ScrollingViewBehavior()
                }
                else -> {
                    binding.topAppBar.visibility = View.GONE
                    binding.bottomNav.visibility = View.GONE
                    mainParams.behavior = null
                }
            }
        }

        // ----- Check Login State -----
        val topMenu = binding.topAppBar.menu
        lifecycleScope.launchWhenStarted {
            viewModel.authStateFlow.collectLatest {
                if (!viewModel.isAvailableToLogIn) {
                    Timber.d("${supportFragmentManager.backStackEntryCount}")
                    navController.navigate(R.id.openLoginFragment)
                } else {
                    if (it?.photoUrl != null) {
                        Firebase.storage.reference.child(it.photoUrl?.path.toString())
                                .downloadUrl.addOnSuccessListener { downloadedUrl ->
                                    topMenu.findItem(R.id.topBarAvatar).actionView.findViewById<ShapeableImageView>(
                                            R.id.avatar
                                    ).load(downloadedUrl)
                                }
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            val notification =
                NotificationCompat.Builder(applicationContext, AppConstants.UPLOAD_CHANNEL_ID)
                    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                    .setContentTitle(getString(R.string.upload_photos))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setGroup(AppConstants.UPLOAD_GROUP)
                    .setGroupSummary(true).build()
            viewModel.worksFlow.collectLatest { works ->
                if (works.isNotEmpty()) {
                    notificationManager.notify(0, notification)
                    topMenu.findItem(R.id.topBarUpload).isVisible = false
                    topMenu.findItem(R.id.topBarIndicator).isVisible = true
                } else {
                    topMenu.findItem(R.id.topBarUpload).isVisible = true
                    topMenu.findItem(R.id.topBarIndicator).isVisible = false
                }
            }
        }

        // ----- Permission & Upload -----
        val getMediaActivityResult =
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
                viewModel.uploadFiles(it)
            }
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                getMediaActivityResult.launch("image/*")
            } else {
                Snackbar.make(
                    binding.root,
                    "Please accept to upload photos.",
                    Snackbar.LENGTH_SHORT
                ).setAction("OK") {
                    // TODO navigate to the Setting Fragment
                }.show()
            }
        }

        // TopAppBar
        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.topBarUpload -> {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    true
                }
                else -> false
            }
        }

        // Note: Use this instead of setOnMenuItemClickListener
        topMenu.findItem(R.id.topBarAvatar).actionView
            .findViewById<ShapeableImageView>(R.id.avatar).setOnClickListener {
                navController.navigate(R.id.openProfileFragment)
            }
    }
}