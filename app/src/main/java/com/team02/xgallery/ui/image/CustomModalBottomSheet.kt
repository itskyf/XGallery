package com.team02.xgallery.ui.image

import android.app.AlertDialog
import android.app.WallpaperManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team02.xgallery.R



class CustomModalBottomSheet(withAppendedId: Uri) : BottomSheetDialogFragment(), View.OnClickListener {
    companion object {
        const val Tag = "CustomBottomSheetDialogFragment"

    }

    private var id = withAppendedId
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.useAsBtn).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.addAlbumBtn).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.moveToArchiveBtn).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.deleteFromDeviceBtn).setOnClickListener(this)
        view.findViewById<EditText>(R.id.imgDes).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.useAsBtn) {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, id)
            val myWallpaperManager = WallpaperManager.getInstance(requireContext())
            val modes = arrayOf("Home Screen", "Lock Screen", "Home Screen and Lock Screen")
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Set Wallpaper")
            builder.setItems(modes) { _, which ->
                when (which) {
                    0 -> myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    1 -> myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    2 -> {
                        myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                        myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    }
                }
            }
            builder.show()
        } else if (v?.id == R.id.addAlbumBtn){
        }
    }

}