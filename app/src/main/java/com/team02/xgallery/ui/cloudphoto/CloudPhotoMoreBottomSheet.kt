package com.team02.xgallery.ui.cloudphoto

import android.app.DownloadManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.databinding.BottomSheetMoreCloudPhotoBinding
import com.team02.xgallery.utils.Utils


class CloudPhotoMoreBottomSheet(private val mediaUri: String) : BottomSheetDialogFragment() {
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
                Firebase.storage.reference.child(mediaUri).getBytes(Long.MAX_VALUE).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    Utils.setPhotoAs(requireContext(), bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                }

            }
        }
    }
}