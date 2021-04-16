package com.team02.xgallery.ui.cloudphoto

import android.annotation.SuppressLint
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
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentCloudPhotoBinding
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CloudPhotoFragment : Fragment() {
    private var _binding: FragmentCloudPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private var showButton = true
    private var isFavorite = false
    private lateinit var mediaId: Any

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloudPhotoBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: CloudPhotoFragmentArgs by navArgs()

        with(binding) {
            imgView.load(ContentUris.withAppendedId(AppConstants.COLLECTION, args.id))
            constraintLayout.setOnClickListener {
                // TODO ???? cai gi day
                if (showButton) {
                    binding.topLayout.visibility = View.GONE
                    binding.bottomLayout.visibility = View.GONE
                    showButton = false
                } else {
                    binding.topLayout.visibility = View.VISIBLE
                    binding.bottomLayout.visibility = View.VISIBLE
                    showButton = true
                }
            }
            moreBtn.setOnClickListener {
                val bottomSheetFragment =
                    CloudPhotoMoreBottomSheet(
                        ContentUris.withAppendedId(
                            AppConstants.COLLECTION,
                            args.id
                        )
                    )
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    bottomSheetFragment.tag
                )

            }
            favoriteBtn.setOnClickListener {
                isFavorite = if (!isFavorite) {
                    binding.favoriteBtn.setImageResource(R.drawable.ic_star_24)
                    true
                } else {
                    binding.favoriteBtn.setImageResource(R.drawable.ic_full_star_24)
                    false
                }
            }
            shareBtn.setOnClickListener {
            }
            backBtn.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}