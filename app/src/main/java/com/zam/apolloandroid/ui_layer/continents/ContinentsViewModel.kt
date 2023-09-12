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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zam.apolloandroid.data_layer.continents.ContinentsRepository
import com.zam.apolloandroid.data_layer.countries.CountriesRepository
import com.zam.apolloandroid.model.Continent
import com.zam.apolloandroid.utils.ApolloContinent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContinentsViewModel @Inject constructor(
    private val continentsRepository: ContinentsRepository,
    private val countriesRepository: CountriesRepository,
) : ViewModel() {

    private var _continents = MutableStateFlow<List<Continent>>(emptyList())
    val continents: StateFlow<List<Continent>> = _continents
    private var _navigateToSettingsFragment = MutableSharedFlow<Boolean>()
    val navigateToSettingsFragment: SharedFlow<Boolean> = _navigateToSettingsFragment

    fun fetchContinentsFullInfo() {
        viewModelScope.launch {
            val continents = continentsRepository.fetchContinents()
            _continents.value = continents.map { continent: ApolloContinent ->
                Continent(continent, getStarredCountriesPercentage(continent.code))
            }
        }
    }

    private suspend fun getStarredCountriesPercentage(code: String): Double {
        val countriesCount = countriesRepository.fetchCountriesCount(code)
        val starredCountriesCount = countriesRepository.fetchStarredCountriesCount(code)

        return (starredCountriesCount * 100) / countriesCount.toDouble()
    }

    fun navigateToSettings() {
        viewModelScope.launch {
            _navigateToSettingsFragment.emit(true)
        }
    }
}