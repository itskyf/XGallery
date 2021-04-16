package com.team02.xgallery.ui.cloudphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team02.xgallery.databinding.BottomSheetMoreCloudPhotoBinding


class CloudPhotoMoreBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetMoreCloudPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetMoreCloudPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            addToAlbumBtn.setOnClickListener {
                // TODO: add this cloud photo to an album
            }
            moveToArchiveBtn.setOnClickListener {
                // TODO: move this cloud photo to Archives
            }
            slideshowBtn.setOnClickListener {
                // TODO: slideshow
            }
            useAsBtn.setOnClickListener {
                // TODO: set this cloud photo as home screen or lock screen
            }
        }
    }
}