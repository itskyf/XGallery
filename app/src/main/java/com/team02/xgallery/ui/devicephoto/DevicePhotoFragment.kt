package com.team02.xgallery.ui.devicephoto

import android.content.ContentUris
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.team02.xgallery.databinding.FragmentDevicePhotoBinding
import com.team02.xgallery.utils.AppConstants

class DevicePhotoFragment : Fragment() {
    private var _binding: FragmentDevicePhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDevicePhotoBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DevicePhotoFragmentArgs by navArgs()
        binding.imgView.load(ContentUris.withAppendedId(AppConstants.COLLECTION, args.id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}