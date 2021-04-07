package com.team02.xgallery.ui.folder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentFolderBinding
import com.team02.xgallery.ui.adapter.AlbumAdapter
import com.team02.xgallery.ui.adapter.AlbumItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FolderFragment : Fragment() {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FolderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)

        val pagingAdapter = AlbumAdapter {
            findNavController().navigate(
                FolderFragmentDirections.openAlbumView(it.id, it.name)
            )
        }
        binding.folderGrid.adapter = pagingAdapter
        binding.folderGrid.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.folderGrid.addItemDecoration(
            AlbumItemDecoration(resources.getDimension(R.dimen.default_padding))
        )
        binding.folderTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        lifecycleScope.launch {
            viewModel.folderPagingFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}