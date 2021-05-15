package com.team02.xgallery.ui.cloudalbummembers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentCloudAlbumMembersBinding
import com.team02.xgallery.databinding.FragmentFavoritesBinding
import com.team02.xgallery.ui.adapter.CloudMediaAdapter
import com.team02.xgallery.ui.adapter.ItemDecoration
import com.team02.xgallery.ui.favorites.FavoritesFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CloudAlbumMembersFragment: Fragment() {
    private var _binding: FragmentCloudAlbumMembersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CloudAlbumMembersViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloudAlbumMembersBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.title = "Members"
        binding.topAppBar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}