package com.team02.xgallery.ui.image

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team02.xgallery.R
import dagger.hilt.android.AndroidEntryPoint
import com.team02.xgallery.databinding.FragmentCloudPhotoBinding



@AndroidEntryPoint
class CloudPhotoFragment : Fragment() {
    private var _binding: FragmentCloudPhotoBinding? = null
    private val binding get() = _binding!!
    private var showButton = true
    private var isFavorite = false
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloudPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.constraintLayout.setOnClickListener{
            if(showButton){
                binding.topLayout.visibility = View.GONE
                binding.bottomLayout.visibility = View.GONE
                showButton = false
            }else{
                binding.topLayout.visibility = View.VISIBLE
                binding.bottomLayout.visibility = View.VISIBLE
                showButton = true
            }
        }
        binding.moreBtn.setOnClickListener{
            val bottomSheetFragment = CustomModalBottomSheet()
            bottomSheetFragment.showNow(requireActivity().supportFragmentManager, bottomSheetFragment.getTag())

        }
        binding.starBtn.setOnClickListener{
            isFavorite = if(!isFavorite){
                binding.starBtn.setImageResource(R.drawable.ic_star_24)
                true
            } else{
                binding.starBtn.setImageResource(R.drawable.ic_full_star_24)
                false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}








