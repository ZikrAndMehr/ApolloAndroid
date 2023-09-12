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

package com.zam.apolloandroid.ui_layer.continents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zam.apolloandroid.databinding.FragmentContinentsBinding
import com.zam.apolloandroid.model.Continent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContinentsFragment : Fragment() {

    private var _binding: FragmentContinentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContinentsViewModel by viewModels()
    private val adapter = ContinentsAdapter { startCountriesFragment(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContinentsBinding.inflate(inflater, container, false)

        viewModel.fetchContinentsFullInfo()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.rvContinents.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.navigateToSettingsFragment.collect {
                        val action = ContinentsFragmentDirections.actionContinentsFragmentToSettingsFragment()
                        findNavController().navigate(action)
                    }
                }
                launch {
                    viewModel.continents.collect { continents ->
                        if (continents != emptyList<Continent>()) {
                            binding.apply {
                                adapter.submitList(continents)
                                continentsShl.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startCountriesFragment(code: String) {
        val action = ContinentsFragmentDirections.actionContinentsFragmentToCountriesFragment(code)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}