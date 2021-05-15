package com.team02.xgallery.ui.devicephoto

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.transition.Hold
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentDevicePhotoBinding
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DevicePhotoFragment : Fragment() {
    private var _binding: FragmentDevicePhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fragment A’s exitTransition can be set any time before Fragment A is
        // replaced with Fragment B. Ensure Hold's duration is set to the same
        // duration as your MaterialContainerTransform.
        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDevicePhotoBinding.inflate(inflater, container, false)
        // Load image
        val args: DevicePhotoFragmentArgs by navArgs()
        val mediaUri = ContentUris.withAppendedId(AppConstants.COLLECTION, args.id)
        val navController = findNavController()
        Glide.with(this).load(mediaUri).into(binding.mediaView)

        // UI
        binding.topBar.background.alpha = 200
        binding.bottomNav.background.alpha = 200
        binding.mediaView.setOnClickListener {
            if (binding.topBar.visibility == View.VISIBLE) {
                binding.topBar.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.topBar.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
        //Top bar
        binding.topBar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.topBar.setOnMenuItemClickListener {
            val bottomSheetFragment = DevicePhotoBottomSheet(mediaUri)
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                bottomSheetFragment.tag
            )
            true
        }

        // Bottom bar
        binding.bottomNav.menu.setGroupCheckable(0, false, true)
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.device_photo_share -> {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_STREAM,
                            ContentUris.withAppendedId(AppConstants.COLLECTION, args.id)
                        )
                        type = "image/jpeg" // TODO image type ?
                    }
                    startActivity(Intent.createChooser(shareIntent, null))
                    true
                }
                R.id.device_photo_edit -> {
                    navController.navigate(
                        DevicePhotoFragmentDirections.actionEditLocalMedia(mediaUri)
                    )
                    true
                }
                R.id.device_photo_delete -> {
                    true
                }
                else -> false
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}