package com.team02.xgallery.ui.image

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
import com.team02.xgallery.Utils
import com.team02.xgallery.databinding.FragmentPhotoBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
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
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PhotoFragmentArgs by navArgs()
        with(binding) {
            imgView.load(ContentUris.withAppendedId(Utils.collection, args.id))
        }

        with(binding) {
            constraintLayout.setOnClickListener {
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
                val bottomSheetFragment = CustomModalBottomSheet(ContentUris.withAppendedId(Utils.collection, args.id))
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    bottomSheetFragment.tag
                )

            }
            starBtn.setOnClickListener {
                isFavorite = if (!isFavorite) {
                    binding.starBtn.setImageResource(R.drawable.ic_star_24)
                    true
                } else {
                    binding.starBtn.setImageResource(R.drawable.ic_full_star_24)
                    false
                }
            }
            shareBtn.setOnClickListener{
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








