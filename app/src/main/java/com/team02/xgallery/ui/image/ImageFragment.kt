package com.team02.xgallery.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team02.xgallery.databinding.FragmentImgBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : Fragment() {
    private var _binding: FragmentImgBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}