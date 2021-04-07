package com.team02.xgallery.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentLibraryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)

        binding.onDeviceButton.setOnClickListener {
            findNavController().navigate(
                LibraryFragmentDirections.actionLibraryFragmentToFolderFragment()
            )
        }

        binding.sortButton.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.album_sort, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                binding.sortButton.text = menuItem.title
                true
            }
            popup.show()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}