package com.team02.xgallery.ui.cloudphoto

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
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.databinding.FragmentCloudPhotoBinding
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CloudPhotoFragment : Fragment() {
    private var _binding: FragmentCloudPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloudPhotoBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CloudPhotoFragmentArgs by navArgs()
        val mediaRef = Firebase.storage.getReference(args.id!!)
        mediaRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Glide.with(binding.root).load(it.result).into(binding.imgView)
            }
        }

        with(binding) {
            backBtn.setOnClickListener {
                navController.popBackStack()
            }
            favoriteBtn.setOnClickListener {
                // TODO: move this cloud photo to Favorites
            }
            moreBtn.setOnClickListener {
                val bottomSheetFragment =
                    CloudPhotoMoreBottomSheet()
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    bottomSheetFragment.tag
                )
            }
            editBtn.setOnClickListener {
                // TODO: edit this cloud photo
            }
            shareBtn.setOnClickListener {
                // TODO: share this cloud photo
            }
            downloadBtn.setOnClickListener {
                // TODO: download this cloud photo
            }
            deleteBtn.setOnClickListener {
                // TODO: move this cloud photo to Trash
            }
            root.setOnClickListener {
                if (appBarsLayout.visibility == View.GONE) {
                    appBarsLayout.visibility = View.VISIBLE
                } else {
                    appBarsLayout.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}