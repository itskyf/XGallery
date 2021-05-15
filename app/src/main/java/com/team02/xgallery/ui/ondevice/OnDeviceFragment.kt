package com.team02.xgallery.ui.ondevice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.R
import com.team02.xgallery.data.entity.Album
import com.team02.xgallery.databinding.FragmentOnDeviceBinding
import com.team02.xgallery.ui.adapter.AlbumAdapter
import com.team02.xgallery.ui.adapter.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnDeviceFragment : Fragment() {
    private var _binding: FragmentOnDeviceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnDeviceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnDeviceBinding.inflate(inflater, container, false)

        val pagingAdapter = AlbumAdapter {
            findNavController().navigate(
                OnDeviceFragmentDirections.openDeviceAlbumView(it.id as Int, it.name)
            )
        }
        binding.folderGrid.adapter = pagingAdapter
        binding.folderGrid.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.folderGrid.addItemDecoration(
            ItemDecoration(resources.getDimension(R.dimen.default_padding), 2)
        )
        binding.folderTopBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        lifecycleScope.launch {
            viewModel.folderPagingFlow.filterIsInstance<PagingData<Album>>()
                .collectLatest { pagingData ->
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