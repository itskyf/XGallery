package com.team02.xgallery.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentLibraryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val images = arrayListOf<Image>(
            Image("Photo_1", R.drawable.download),
            Image("Photo_1", R.drawable.download1),
            Image("Photo_1", R.drawable.download2),
            Image("Photo_1", R.drawable.download4),
            Image("Photo_1", R.drawable.download5),
            Image("Photo_1", R.drawable.download6),
            Image("Photo_1", R.drawable.images),
        )
        val adapter = ImageAdapter(images)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.viewgroup.layoutManager = manager
        binding.viewAlbum.adapter = adapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}