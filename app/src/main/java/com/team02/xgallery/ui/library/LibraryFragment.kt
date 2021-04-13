package com.team02.xgallery.ui.library

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // ----- External Storage Permission -----
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                findNavController().navigate(
                    LibraryFragmentDirections.actionLibraryFragmentToOnDeviceFragment()
                )
            } else {
                Snackbar.make(
                    binding.root,
                    "Please accept to upload photos.",
                    Snackbar.LENGTH_SHORT
                ).setAction("OK") {
                    // TODO Navigate to the Setting Fragment (ua vay con cai o MainActivity?)
                    // navController.navigate(R.id.setting_fragment)
                }.show()
            }
        }

        with(binding) {
            onDeviceButton.setOnClickListener {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            sortButton.setOnClickListener {
                val popup = PopupMenu(requireContext(), it)
                popup.menuInflater.inflate(R.menu.album_sort, popup.menu)
                popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                    binding.sortButton.text = menuItem.title
                    true
                }
                popup.show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}