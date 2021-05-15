package com.team02.xgallery.ui.devicephoto

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team02.xgallery.databinding.BottomSheetDevicePhotoBinding
import com.team02.xgallery.utils.Utils

class DevicePhotoBottomSheet(private val mediaUri: Uri) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDevicePhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDevicePhotoBinding.inflate(inflater, container, false)
        binding.slideshowBtn.setOnClickListener {
            // TODO: slideshow
        }
        binding.useAsBtn.setOnClickListener {
            val context = requireContext()
            Glide.with(context)
                .asBitmap()
                .load(mediaUri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        TODO("Not yet implemented")
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Utils.setPhotoAs(context, resource)
                    }
                })
        }

        return binding.root
    }
}