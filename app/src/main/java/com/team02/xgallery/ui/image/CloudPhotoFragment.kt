package com.team02.xgallery.ui.image

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentCloudPhotoBinding
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CloudPhotoFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentCloudPhotoBinding? = null
    private val binding get() = _binding!!
    private var showButton = true
    private var isFavorite = false
    private var db = FirebaseStorage.getInstance().getReference("admin")
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
            val popup = PopupMenu(activity, view)
            popup.gravity = Gravity.TOP
            popup.inflate(R.menu.menu_img_more)
            popup.setOnMenuItemClickListener { item -> menuItemClicked(item) }
            popup.show()
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

    private fun menuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.more -> Toast.makeText(activity, "Bookmark", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }
}








