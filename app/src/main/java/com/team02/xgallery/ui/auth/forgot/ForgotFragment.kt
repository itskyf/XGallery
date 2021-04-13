package com.team02.xgallery.ui.auth.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.team02.xgallery.databinding.FragmentForgotBinding
import com.team02.xgallery.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotFragment : Fragment() {
    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgotViewModel by viewModels()
    private var uiStateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        uiStateJob = lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    ForgotState.SUCCESS -> {
                        Snackbar.make(
                            binding.root,
                            "The reset link has been sent to your email.\nCheck it out!",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("OK") {
                                navController.popBackStack()
                            }
                            .show()
                    }
                    ForgotState.INPUT -> {
                        Utils.setViewAndChildrenEnabled(binding.form, true)
                        binding.progressBar.visibility = View.GONE
                    }
                    ForgotState.LOADING -> {
                        Utils.setViewAndChildrenEnabled(binding.form, false)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    ForgotState.MALFORMED_EMAIL -> {
                        Snackbar.make(
                            binding.root,
                            "Your email address is malformed.\nPlease try again!",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                        viewModel.tryAgain()
                    }
                    ForgotState.NOT_EXISTING_EMAIL -> {
                        Snackbar.make(
                            binding.root,
                            "Your email address does not exist.\nPlease try again!",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                        viewModel.tryAgain()
                    }
                    ForgotState.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            "Unexpected error.\nPlease try again!",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                        viewModel.tryAgain()
                    }
                }
            }
        }

        with(binding) {
            btnCancel.setOnClickListener {
                navController.popBackStack()
            }
            btnSend.setOnClickListener {
                val email = binding.tiEmail.text.toString()
                lifecycleScope.launch {
                    viewModel.resetPassword(email)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        uiStateJob?.cancel()
        super.onStop()
    }
}