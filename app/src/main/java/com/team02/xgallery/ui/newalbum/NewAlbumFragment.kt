package com.team02.xgallery.ui.newalbum

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.team02.xgallery.R
import com.team02.xgallery.databinding.FragmentNewAlbumBinding
import javax.inject.Inject


class NewAlbumFragment: Fragment()  {
    private var _binding: FragmentNewAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.buttonBack.setOnClickListener {
            navController.navigate(R.id.actionNewAlbumFragmentLibraryFragment)
        }
        binding.buttonDone.setOnClickListener {
            navController.navigate(R.id.actionNewAlbumFragmentLibraryFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}