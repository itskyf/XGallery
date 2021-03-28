package com.team02.xgallery.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.team02.xgallery.Utils
import com.team02.xgallery.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: RegisterViewModel by viewModels()
    private var uiStateJob: Job? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        uiStateJob = lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    RegisterState.SUCCESS -> {
                        Snackbar.make(binding.root, "The verification link has been sent to your email.\nCheck it out!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK") {
                                    navController.popBackStack()
                                }
                                .show()
                    }
                    RegisterState.INPUT -> {
                        Utils.setViewAndChildrenEnabled(binding.form, true)
                        binding.progressBar.visibility = View.GONE
                    }
                    RegisterState.LOADING -> {
                        Utils.setViewAndChildrenEnabled(binding.form, false)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    RegisterState.MALFORMED_EMAIL -> {
                        Snackbar.make(binding.root, "Your email address is malformed.\nPlease try again!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                    RegisterState.WEAK_PASSWORD -> {
                        Snackbar.make(binding.root, "Your password must have at least 6 characters.\nPlease try again!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                    RegisterState.EXISTING_EMAIL -> {
                        Snackbar.make(binding.root, "That email address has been taken already.\nPlease try another one!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                    RegisterState.ERROR -> {
                        Snackbar.make(binding.root, "Unexpected error.\nPlease try again!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                }
            }
        }

        with(binding) {
            btnRegister.setOnClickListener {
                val displayName = tiDisplayName.text.toString()
                val email = tiEmail.text.toString()
                val password = tiPassword.text.toString()
                lifecycleScope.launch {
                    viewModel.register(displayName, email, password)
                }
            }
            tvLogin.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}