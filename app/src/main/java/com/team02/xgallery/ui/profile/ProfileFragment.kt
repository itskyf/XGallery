package com.team02.xgallery.ui.profile

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.team02.xgallery.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----- Permission & Upload -----
        val getMediaActivityResult =
                registerForActivityResult(ActivityResultContracts.GetContent()) {
                    lifecycleScope.launch {
                        viewModel.setUserAvatarFromDevice(it)
                        Firebase.storage.reference.child(viewModel.userAvatar?.path.toString())
                            .downloadUrl.addOnSuccessListener { downloadedUrl ->
                                binding.userAvatar.load(downloadedUrl)
                            }
                    }
                }
        val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
        ) { granted: Boolean ->
            if (granted) {
                getMediaActivityResult.launch("image/*")
            } else {
                Snackbar.make(
                        binding.root,
                        "Please accept to set avatar.",
                        Snackbar.LENGTH_SHORT
                ).setAction("OK") {
                    // TODO navigate to the Setting Fragment
                }.show()
            }
        }

        with(binding) {
            Firebase.storage.reference.child(viewModel.userAvatar?.path.toString())
                .downloadUrl.addOnSuccessListener { downloadedUrl ->
                    binding.userAvatar.load(downloadedUrl)
                }
            userDisplayName.text = viewModel.userName
            email.text = viewModel.userEmail
            userAvatar.setOnClickListener {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            backBtn.setOnClickListener {
                navController.popBackStack()
            }
            signOutBtn.setOnClickListener {
                viewModel.signOut()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}