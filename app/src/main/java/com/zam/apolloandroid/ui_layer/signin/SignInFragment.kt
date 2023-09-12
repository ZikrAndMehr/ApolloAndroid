/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zam.apolloandroid.ui_layer.signin

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
import com.zam.apolloandroid.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.checkCurrentUser()

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
                        signedIn.collect { currentUser ->
                            when (currentUser) {
                                CurrentUser.SIGNED_IN -> {
                                    val action = SignInFragmentDirections.actionSignInFragmentToContinentsFragment()
                                    findNavController().navigate(action)
                                }
                                CurrentUser.NOT_SIGNED_IN -> Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                                CurrentUser.UNKNOWN -> {}
                            }
                        }
                    }
                    launch {
                        navigateToForgotPasswordFragment.collect {
                            val action = SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
                            findNavController().navigate(action)
                        }
                    }
                    launch {
                        navigateToSignUpFragment.collect {
                            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
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