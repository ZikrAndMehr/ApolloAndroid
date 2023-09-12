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

package com.zam.apolloandroid.ui_layer.countries

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.zam.apolloandroid.databinding.FragmentCountriesBinding
import com.zam.apolloandroid.model.Country
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountriesFragment : Fragment() {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountriesViewModel by viewModels()
    private val adapter = CountriesAdapter({ viewModel.starCountry(it) }, { viewModel.unStarCountry(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountriesBinding.inflate(inflater, container, false)
        val args: CountriesFragmentArgs by navArgs()
        val code = args.code

        viewModel.fetchCountriesInfo(code)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.rvCountries.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFlows()
        setUpSearch()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countries.collect { countries ->
                    if (countries != emptyList<Country>()) {
                        adapter.submitList(countries)
                        binding.countriesShl.apply {
                            stopShimmer()
                            visibility = View.GONE
                        }
                        binding.rvCountries.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setUpSearch() {
        binding.svCountries.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.searchFilter(newText, viewModel.countries.value)
                return false
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}