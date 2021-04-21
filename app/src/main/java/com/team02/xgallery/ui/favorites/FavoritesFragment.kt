package com.team02.xgallery.ui.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.databinding.FragmentFavoritesBinding
import com.team02.xgallery.ui.adapter.CloudMediaAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteTopBar.title = "Favorites"
        binding.favoriteTopBar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        val pagingAdapter = CloudMediaAdapter({
            navController.navigate(FavoritesFragmentDirections.openFavoritePhotoView(it.id!!))
        }, viewModel.selectionManager)
        binding.favoriteMediaGrid .adapter = pagingAdapter
        binding.favoriteMediaGrid.layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
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