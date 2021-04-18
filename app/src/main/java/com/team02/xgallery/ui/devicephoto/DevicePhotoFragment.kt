package com.team02.xgallery.ui.devicephoto

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.android.material.snackbar.Snackbar
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentDevicePhotoBinding
import com.team02.xgallery.utils.AppConstants

class DevicePhotoFragment : Fragment() {
    private var _binding: FragmentDevicePhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDevicePhotoBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DevicePhotoFragmentArgs by navArgs()
        binding.imgView.setImage(
            ImageSource.uri(ContentUris.withAppendedId(AppConstants.COLLECTION, args.id)))

        with(binding) {
            backBtn.setOnClickListener {
                navController.popBackStack()
            }
            moreBtn.setOnClickListener {
                val bottomSheetFragment =
                    DevicePhotoMoreBottomSheet(
                        ContentUris.withAppendedId(
                            AppConstants.COLLECTION,
                            args.id
                        )
                    )
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    bottomSheetFragment.tag
                )
            }
            shareBtn.setOnClickListener {
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, ContentUris.withAppendedId(AppConstants.COLLECTION, args.id))
                    type = "image/jpeg"
                }
                startActivity(Intent.createChooser(shareIntent, null))
            }
            editBtn.setOnClickListener {
                // TODO: edit this device photo
            }
            uploadBtn.setOnClickListener {
                // TODO: upload this device photo
            }
            deleteBtn.setOnClickListener {
                Snackbar.make(
                    binding.root,
                    "Upcoming feature",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            imgView.setOnClickListener {
                if (appBarsLayout.visibility == View.GONE) {
                    appBarsLayout.visibility = View.VISIBLE
                } else {
                    appBarsLayout.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}