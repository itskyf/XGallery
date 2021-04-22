package com.team02.xgallery.ui.cloudphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentCloudPhotoBinding
import com.team02.xgallery.utils.SubsamplingScaleImageViewTarget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CloudPhotoFragment : Fragment() {
    private var _binding: FragmentCloudPhotoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CloudPhotoViewModel by viewModels()
    private lateinit var navController: NavController
    private var favoriteStateJob: Job? = null
    private var deletedStateJob: Job? = null

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
        val mediaId = args.id

        viewModel.initState(mediaId)

        val mediaRef = Firebase.storage.getReference(mediaId)
        mediaRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Glide.with(view.context).download(it.result).into(SubsamplingScaleImageViewTarget(binding.imgView))
            }
        }

        favoriteStateJob = lifecycleScope.launch {
            viewModel.isFavorite.collect { isFavorite ->
                if (isFavorite) {
                    binding.favoriteBtn.setImageResource(R.drawable.ic_full_star_24)
                } else {
                    binding.favoriteBtn.setImageResource(R.drawable.ic_star_24)
                }
            }
        }

        deletedStateJob = lifecycleScope.launch {
            viewModel.isDeleted.collect { isDeleted ->
                if (isDeleted) {
                    navController.popBackStack()
                }
            }
        }

        with(binding) {
            backBtn.setOnClickListener {
                navController.popBackStack()
            }
            favoriteBtn.setOnClickListener {
                viewModel.updateFavoriteState(mediaId)
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
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Move to trash?")
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.cancel()
                    }
                    .setPositiveButton("Accept") { dialog, which ->
                        viewModel.deleteMedia(mediaId)
                        dialog.cancel()
                    }
                    .show()
            }
            imgView.setOnClickListener {
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

    override fun onStop() {
        favoriteStateJob?.cancel()
        deletedStateJob?.cancel()
        super.onStop()
    }
}