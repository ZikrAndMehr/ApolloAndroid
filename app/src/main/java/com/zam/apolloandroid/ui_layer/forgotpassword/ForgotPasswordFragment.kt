package com.zam.apolloandroid.ui_layer.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zam.apolloandroid.R
import com.zam.apolloandroid.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.apply {
                    launch {
                        sentPasswordReset.collect { sent: Boolean ->
                            if (sent) {
                                Toast.makeText(context, R.string.check_email, Toast.LENGTH_SHORT).show()
                                val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment()
                                findNavController().navigate(action)
                            }
                            else Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                        }
                    }
                    launch {
                        navigateToSignInFragment.collect {
                            val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignInFragment()
                            findNavController().navigate(action)
                        }
                    }
                    launch {
                        navigateToSignUpFragment.collect {
                            val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignUpFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}