package com.team02.xgallery.ui.auth.login

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
import com.team02.xgallery.R
import com.team02.xgallery.Utils
import com.team02.xgallery.databinding.FragmentLoginBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        uiStateJob = lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    LoginState.SUCCESS -> {
                        navController.navigate(R.id.photos_fragment)
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
                        Snackbar.make(binding.root, "Your email has not verified yet. Please verify it first!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                    LoginState.NOT_EXISTING_EMAIL -> {
                        Snackbar.make(binding.root, "Your email does not exist. Please try again!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                    LoginState.WRONG_PASSWORD -> {
                        Snackbar.make(binding.root, "Your password is not correct. Please try again!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                    LoginState.ERROR -> {
                        Snackbar.make(binding.root, "Unexpected error. Please try again!", Snackbar.LENGTH_SHORT)
                                .show()
                        viewModel.tryAgain()
                    }
                }
            }
        }

        with(binding) {
            tvForgot.setOnClickListener {

            }
            binding.btnLogin.setOnClickListener {
                val email = binding.tiEmail.text.toString()
                val password = binding.tiPassword.text.toString()
                lifecycleScope.launch {
                    viewModel.signIn(email, password)
                }
            }
            btnLoginGoogle.setOnClickListener {

            }
            tvRegister.setOnClickListener {
                navController.navigate(R.id.register_fragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}