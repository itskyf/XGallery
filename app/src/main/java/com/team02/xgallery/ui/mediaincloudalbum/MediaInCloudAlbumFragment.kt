package com.team02.xgallery.ui.mediaincloudalbum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentFavoritesBinding
import com.team02.xgallery.databinding.FragmentMediaInCloudAlbumBinding
import com.team02.xgallery.ui.adapter.CloudMediaAdapter
import com.team02.xgallery.ui.adapter.ItemDecoration
import com.team02.xgallery.ui.collections.CollectionsFragmentDirections
import com.team02.xgallery.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaInCloudAlbumFragment : Fragment() {
    private var _binding: FragmentMediaInCloudAlbumBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaInCloudAlbumViewModel by viewModels()
    private lateinit var navController: NavController
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

        val nameAlbum = args.nameOfAlbum

        binding.mediaInCloudAlbumTopBar.title = nameAlbum
        binding.mediaInCloudAlbumTopBar.setNavigationOnClickListener {
            navController.navigateUp()
        }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}