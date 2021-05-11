package com.team02.xgallery.ui.mediaincloudalbum

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.team02.xgallery.MainActivity
import com.team02.xgallery.R
import com.team02.xgallery.data.repository.CloudAlbumRepository
import com.team02.xgallery.databinding.FragmentFavoritesBinding
import com.team02.xgallery.databinding.FragmentMediaInCloudAlbumBinding
import com.team02.xgallery.ui.adapter.CloudMediaAdapter
import com.team02.xgallery.ui.adapter.ItemDecoration
import com.team02.xgallery.ui.collections.CollectionsFragmentDirections
import com.team02.xgallery.ui.home.HomeFragmentDirections
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaInCloudAlbumFragment : Fragment() {
    private var _binding: FragmentMediaInCloudAlbumBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaInCloudAlbumViewModel by viewModels()
    private lateinit var navController: NavController
    private var selectionMode: ActionMode? = null
    private var onSelectionModeJob: Job? = null
    private var selectedCountJob: Job? = null
    private var CloudAlbumRepositoryVar : CloudAlbumRepository = CloudAlbumRepository()
    val args: MediaInCloudAlbumFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaInCloudAlbumBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idAlbum = args.IdOfAlbum
        val nameAlbum = args.nameOfAlbum
        binding.mediaInCloudAlbumTopBar.setNavigationOnClickListener {
            navController.navigate(
                MediaInCloudAlbumFragmentDirections.actionMediaInCloudAlbumFragmentToCollectionsFragment()
            )
        }

        binding.mediaInCloudAlbumTopBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.select_photos -> {
                    true
                }
                R.id.edit_album -> {
                    true
                }
                R.id.delete_album -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage(resources.getString(R.string.ask_delete_album))
                        .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                            CloudAlbumRepositoryVar.deleteAlbum(args.IdOfAlbum)
                            navController.navigate(
                                MediaInCloudAlbumFragmentDirections.actionMediaInCloudAlbumFragmentToCollectionsFragment()
                            )
                        }
                        .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                        }
                        .show()

                    true
                }
                else -> false
            }
        }

        binding.nameAlbum.setText(nameAlbum)

        val pagingAdapter = CloudMediaAdapter({
            navController.navigate(HomeFragmentDirections.openCloudPhotoViewFromHome(it.id!!))
        }, viewModel.selectionManager)
        binding.mediaInCloudAlbumGrid.adapter = pagingAdapter
        binding.mediaInCloudAlbumGrid.layoutManager =
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding.mediaInCloudAlbumGrid.addItemDecoration(
            ItemDecoration(resources.getDimension(R.dimen.small_padding), 3)
        )

        lifecycleScope.launch {
            viewModel.mediaPagingFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        onSelectionModeJob = lifecycleScope.launch {
            viewModel.selectionManager.onSelectionMode.collectLatest { onSelectionMode ->
                if (onSelectionMode) {
                    selectionMode =
                        (requireActivity() as MainActivity).startSupportActionMode(callback) as ActionMode
                } else {
                    selectionMode?.finish()
                }
                pagingAdapter.notifyDataSetChanged()
            }
        }

        selectedCountJob = lifecycleScope.launch {
            viewModel.selectionManager.selectedCount.collectLatest { selectedCount ->
                selectionMode?.title = "$selectedCount"
            }
        }
    }

    override fun onStop() {
        onSelectionModeJob?.cancel()
        selectedCountJob?.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val callback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.device_album_contextual_top_app_bar, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(
            mode: ActionMode?,
            item: MenuItem?
        ): Boolean {
            return when (item?.itemId) {
                R.id.share -> {
                    true
                }
                R.id.delete -> {
                    val listItem = viewModel.selectionManager.getItemKeyList()
                    val idAlbum = args.IdOfAlbum

                    CloudAlbumRepositoryVar.deletePhotos(idAlbum,listItem)

                    onDestroyActionMode(mode)
                    true
                }
                R.id.more -> {
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            viewModel.selectionManager.clear()
        }
    }
}