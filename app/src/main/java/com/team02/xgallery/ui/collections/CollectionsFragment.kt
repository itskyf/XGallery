package com.team02.xgallery.ui.collections

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentCollectionsBinding
import com.team02.xgallery.ui.adapter.CloudAlbumAdapter
import com.team02.xgallery.ui.home.CollectionsViewModel
import com.team02.xgallery.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionsFragment : Fragment() {
    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: CollectionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // ----- External Storage Permission -----
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                findNavController().navigate(
                    CollectionsFragmentDirections.actionLibraryFragmentToOnDeviceFragment()
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

        val cloudAlbumPagingAdapter = CloudAlbumAdapter()
        binding.gridAlbum.adapter = cloudAlbumPagingAdapter
        binding.gridAlbum.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        lifecycleScope.launch {
            viewModel.albumPagingFlow.collectLatest { pagingData ->
                cloudAlbumPagingAdapter.submitData(pagingData)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}