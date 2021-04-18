package com.team02.xgallery.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentHomeBinding
import com.team02.xgallery.ui.adapter.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val listImg = listOf(R.drawable.a, R.drawable.b, R.drawable.a, R.drawable.b)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewModel.isAvailableToLogIn) {
            navController.navigate(R.id.loginFragment)
        }
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val storyAdapter = StoryAdapter(listImg)
        binding.storyList.adapter = storyAdapter
        binding.storyList.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}