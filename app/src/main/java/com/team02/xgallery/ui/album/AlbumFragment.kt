package com.team02.xgallery.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.databinding.FragmentAlbumBinding
import com.team02.xgallery.ui.adapter.MediaAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : Fragment() {
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumFragmentArgs by navArgs()
    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        binding.albumTopBar.title = args.albumName
        binding.albumTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val pagingAdapter = MediaAdapter {
            // TODO navigation
        }
        binding.albumMediaGrid.adapter = pagingAdapter
        binding.albumMediaGrid.layoutManager =
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        lifecycleScope.launch {
            viewModel.mediaPagingFlow.collectLatest { pagingData ->
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