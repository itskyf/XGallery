package com.team02.xgallery.ui.cloudphoto

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
                Firebase.storage.reference.child(mediaUri).getBytes(1024*1024).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    Utils.setCloudPhotoAs(requireContext(), bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                }

            }
        }
    }
    @Synchronized
    private fun downloadFile(context: Context, fileName: String, fileExtension: String, destinationDirectory: String, url: String) {
        val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri: Uri = Uri.parse(url)
        val request: DownloadManager.Request = DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request)

    }
}