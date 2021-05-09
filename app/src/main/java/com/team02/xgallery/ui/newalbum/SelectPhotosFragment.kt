package com.team02.xgallery.ui.newalbum

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.team02.xgallery.MainActivity
import com.team02.xgallery.R
import com.team02.xgallery.data.repository.CloudAlbumRepository
import com.team02.xgallery.databinding.FragmentHomeBinding
import com.team02.xgallery.databinding.FragmentSelectPhotosBinding
import com.team02.xgallery.ui.adapter.CloudMediaAdapter
import com.team02.xgallery.ui.adapter.ItemDecoration
import com.team02.xgallery.ui.adapter.SelectPhotosAdapter
import com.team02.xgallery.ui.adapter.StoryAdapter
import com.team02.xgallery.ui.home.HomeFragmentDirections
import com.team02.xgallery.ui.mediaincloudalbum.MediaInCloudAlbumFragmentArgs
import com.team02.xgallery.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SelectPhotosFragment : Fragment() {
    private var _binding: FragmentSelectPhotosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SelectPhotosViewModel by viewModels()
    private lateinit var navController: NavController
    private var selectionMode: ActionMode? = null
    private var onSelectionModeJob: Job? = null
    private var selectedCountJob: Job? = null
    val args: SelectPhotosFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPhotosBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectPhotosTopBar.title = "Add photos"
        binding.selectPhotosTopBar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        val cloudMediaPagingAdapter = SelectPhotosAdapter(viewModel.selectionManager)
        binding.selectPhotosMediaGrid.adapter = cloudMediaPagingAdapter
        binding.selectPhotosMediaGrid.layoutManager =
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding.selectPhotosMediaGrid.addItemDecoration(
            ItemDecoration(resources.getDimension(R.dimen.small_padding), 3)
        )

        lifecycleScope.launch {
            viewModel.mediaPagingFlow.collectLatest { pagingData ->
                cloudMediaPagingAdapter.submitData(pagingData)
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
                cloudMediaPagingAdapter.notifyDataSetChanged()
            }
        }

        selectedCountJob = lifecycleScope.launch {
            viewModel.selectionManager.selectedCount.collectLatest { selectedCount ->
                selectionMode?.title = "$selectedCount"
            }
        }
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
            mode?.menuInflater?.inflate(R.menu.select_photos_top_bar, menu)
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
                R.id.done -> {
                    val listItem = viewModel.selectionManager.getItemKeyList()
                    val name = args.name

                    if (name != "") {
                        CloudAlbumRepository().createAlbum(name,listItem)
                    } else {
                        CloudAlbumRepository().createAlbum("Nameless",listItem)
                    }
                    navController.navigate(R.id.actionSelectPhotosFragmentToCollectionsFragment)
                    onDestroyActionMode(mode)
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