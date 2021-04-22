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
import com.team02.xgallery.data.repository.CloudAlbumRepository
import com.team02.xgallery.databinding.FragmentCollectionsBinding
import com.team02.xgallery.ui.adapter.ItemDecoration
import com.team02.xgallery.ui.adapter.CloudAlbumAdapter
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
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----- External Storage Permission -----
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                navController.navigate(
                    CollectionsFragmentDirections.actionLibraryFragmentToOnDeviceFragment()
                )
            } else {
                Snackbar.make(
                    binding.root,
                    "Please accept to upload photos.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        with(binding) {
            favoriteButton.setOnClickListener {
                navController.navigate(
                    CollectionsFragmentDirections.actionLibraryFragmentToFavoritesFragment()
                )
            }

            archiveButton.setOnClickListener {
                // TODO
                CloudAlbumRepository().createAlbum()
            }

            onDeviceButton.setOnClickListener {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            trashButton.setOnClickListener {
                navController.navigate(
                    CollectionsFragmentDirections.actionLibraryFragmentToTrashFragment()
                )
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

        val cloudAlbumPagingAdapter = CloudAlbumAdapter {
            Log.d("KCH", "${it.name}")
        }
        binding.gridAlbum.adapter = cloudAlbumPagingAdapter
        binding.gridAlbum.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.gridAlbum.addItemDecoration(
            ItemDecoration(resources.getDimension(R.dimen.default_padding), 2)
        )
        lifecycleScope.launch {
            viewModel.albumPagingFlow.collectLatest { pagingData ->
                cloudAlbumPagingAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}