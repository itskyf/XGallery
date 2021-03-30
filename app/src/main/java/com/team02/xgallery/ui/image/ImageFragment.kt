package com.team02.xgallery.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentImgBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImageFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.moreBtn.setOnClickListener{
            val popup = PopupMenu(activity, view)
            popup.inflate(R.menu.menu_img_more)
            val menu = popup.menu
            popup.setOnMenuItemClickListener { item -> menuItemClicked(item) }
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
    override fun onMenuItemClick(item: MenuItem): Boolean {
        return true
    }
}






