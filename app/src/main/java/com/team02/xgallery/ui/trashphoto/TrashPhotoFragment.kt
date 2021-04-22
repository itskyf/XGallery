package com.team02.xgallery.ui.trashphoto

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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.databinding.FragmentTrashPhotoBinding
import com.team02.xgallery.ui.cloudphoto.CloudPhotoFragmentArgs
import com.team02.xgallery.utils.SubsamplingScaleImageViewTarget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrashPhotoFragment : Fragment() {
    private var _binding: FragmentTrashPhotoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TrashPhotoViewModel by viewModels()
    private lateinit var navController: NavController
    private var restoreStateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrashPhotoBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: CloudPhotoFragmentArgs by navArgs()
        val mediaId = args.id

        val mediaRef = Firebase.storage.getReference(mediaId)
        mediaRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Glide.with(view.context).download(it.result).into(SubsamplingScaleImageViewTarget(binding.imgView))
            }
        }

        restoreStateJob = lifecycleScope.launch {
            viewModel.restoreState.collectLatest { restored ->
                if (restored) {
                    navController.popBackStack()
                }
            }
        }

        with(binding) {
            deleteBtn.setOnClickListener {
                // TODO
            }
            restoreBtn.setOnClickListener {
                viewModel.restoreMedia(mediaId)
            }
            trashPhotoTopBar.setNavigationOnClickListener {
                navController.navigateUp()
            }
            imgView.setOnClickListener {
                if (trashPhotoTopBar.visibility == View.GONE) {
                    trashPhotoTopBar.visibility = View.VISIBLE
                    trashPhotoBottomBar.visibility = View.VISIBLE
                } else {
                    trashPhotoTopBar.visibility = View.GONE
                    trashPhotoBottomBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        restoreStateJob?.cancel()
        super.onStop()
    }
}