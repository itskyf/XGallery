package com.team02.xgallery.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.team02.xgallery.databinding.FragmentLoginBinding
import com.team02.xgallery.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: LoginViewModel by viewModels()
    private var uiStateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiStateJob = lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    LoginState.SUCCESS -> {
                        navController.navigate(navController.graph.startDestination)
                    }
                    LoginState.INPUT -> {
                        Utils.setViewAndChildrenEnabled(binding.form, true)
                        binding.progressBar.visibility = View.GONE
                    }
                    LoginState.LOADING -> {
                        Utils.setViewAndChildrenEnabled(binding.form, false)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    LoginState.NOT_VERIFIED -> {
                        Snackbar.make(
                            binding.root,
                            "Your email address has not been verified yet. Please verify it first!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        viewModel.tryAgain()
                    }
                    LoginState.NOT_EXISTING_EMAIL -> {
                        Snackbar.make(
                            binding.root,
                            "Your email address does not exist. Please try again!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        viewModel.tryAgain()
                    }
                    LoginState.WRONG_PASSWORD -> {
                        Snackbar.make(
                            binding.root,
                            "Your password is not correct. Please try again!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        viewModel.tryAgain()
                    }
                    LoginState.ERROR -> {
                        Snackbar.make(
                            binding.root,
                            "Unexpected error. Please try again!",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                        viewModel.tryAgain()
                    }
                }
            }
        }

        binding.tvForgot.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment())
        }
        binding.tiPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.signIn(
                    email = binding.tiEmail.text.toString(),
                    password = binding.tiPassword.text.toString()
                )
            }
            true
        }
        binding.btnLogin.setOnClickListener {
            viewModel.signIn(
                email = binding.tiEmail.text.toString(),
                password = binding.tiPassword.text.toString()
            )
        }
        binding.btnLoginGoogle.setOnClickListener {
            // TODO login with Google
        }
        binding.tvRegister.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
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