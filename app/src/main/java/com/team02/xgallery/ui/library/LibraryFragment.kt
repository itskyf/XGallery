package com.team02.xgallery.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.data.entity.Media
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arrayListOf<Media>(
                Media("Album 1"),
                Media("Album 2"),
                Media("Album 3"),
                Media("Album 4"),
                Media("Album 5"),
                Media("Album 6"),
                Media("Album 7"),
                Media("Album 8"),
                Media("Album 9"),
        )
        val adapter = ImageAdapter(data)
        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.gridAlbum.layoutManager = manager
        binding.gridAlbum.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}