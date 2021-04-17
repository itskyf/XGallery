package com.team02.xgallery.ui.devicealbum

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.MainActivity
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentDeviceAlbumBinding
import com.team02.xgallery.ui.adapter.LocalMediaAdapter
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviceAlbumFragment : Fragment() {
    private var _binding: FragmentDeviceAlbumBinding? = null
    private val binding get() = _binding!!
    private val args: DeviceAlbumFragmentArgs by navArgs()
    private val viewModel: FolderViewModel by viewModels()
    private lateinit var navController: NavController
    private var selectionMode: ActionMode? = null
    private var onSelectionModeJob: Job? = null
    private var selectedCountJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceAlbumBinding.inflate(inflater, container, false)
        navController = findNavController()

        binding.albumTopBar.title = args.albumName
        binding.albumTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val pagingAdapter = LocalMediaAdapter({
            navController.navigate(
                DeviceAlbumFragmentDirections.openDevicePhotoView(it.id)
            )
        }, viewModel.selectionManager)

        binding.albumMediaGrid.adapter = pagingAdapter

        binding.albumMediaGrid.layoutManager =
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)

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
                    binding.albumTopBar.visibility = View.INVISIBLE
                } else {
                    selectionMode?.finish()
                    binding.albumTopBar.visibility = View.VISIBLE
                }
                pagingAdapter.notifyDataSetChanged()
            }
        }

        selectedCountJob = lifecycleScope.launch {
            viewModel.selectionManager.selectedCount.collectLatest { selectedCount ->
                selectionMode?.title = "$selectedCount"
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        onSelectionModeJob?.cancel()
        selectedCountJob?.cancel()
        super.onStop()
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
                    val arrayImage = viewModel.selectionManager.getItemKeyList()
                    val imageUris: ArrayList<Uri> = ArrayList()
                    for (i in arrayImage){
                        imageUris.add(ContentUris.withAppendedId(AppConstants.COLLECTION, i as Long))
                    }
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND_MULTIPLE
                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
                        type = "image/*"
                    }
                    startActivity(Intent.createChooser(shareIntent, "Share images to.."))
                    true
                }
                R.id.delete -> {
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