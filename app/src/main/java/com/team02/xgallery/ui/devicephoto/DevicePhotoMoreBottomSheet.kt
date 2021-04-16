package com.team02.xgallery.ui.devicephoto

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team02.xgallery.databinding.BottomSheetMoreDevicePhotoBinding
import com.team02.xgallery.utils.Utils

class DevicePhotoMoreBottomSheet(private val mediaUri: Uri) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetMoreDevicePhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetMoreDevicePhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            slideshowBtn.setOnClickListener {
                // TODO: slideshow
            }
            useAsBtn.setOnClickListener {
                Utils.setDevicePhotoAs(requireContext(), mediaUri)
            }
        }
    }
}