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