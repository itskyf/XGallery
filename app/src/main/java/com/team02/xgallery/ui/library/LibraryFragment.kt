package com.team02.xgallery.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.data.entity.MyMedia
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

        val data = arrayListOf<MyMedia>(
                MyMedia("Album 1"),
                MyMedia("Album 2"),
                MyMedia("Album 3"),
                MyMedia("Album 4"),
                MyMedia("Album 5"),
                MyMedia("Album 6"),
                MyMedia("Album 7"),
                MyMedia("Album 8"),
                MyMedia("Album 9"),
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