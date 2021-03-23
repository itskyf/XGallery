package com.team02.xgallery.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
            Image("Photo_1", R.drawable.ic_launcher_foreground),
            Image("Photo_2", R.drawable.ic_launcher_foreground),
            Image("Photo_3", R.drawable.ic_launcher_foreground),
            Image("Photo_4", R.drawable.ic_launcher_foreground),
            Image("Photo_5", R.drawable.ic_launcher_foreground),
            Image("Photo_6", R.drawable.ic_launcher_foreground),
            Image("Photo_7", R.drawable.ic_launcher_foreground),
            Image("Photo_8", R.drawable.ic_launcher_foreground),
            Image("Photo_9", R.drawable.ic_launcher_foreground),
            Image("Photo_10", R.drawable.ic_launcher_foreground),
        )
        val adapter = this.activity?.let { ImageAdapter(it, images) }
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.viewgroup.layoutManager = manager
        binding.viewgroup.adapter = adapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}