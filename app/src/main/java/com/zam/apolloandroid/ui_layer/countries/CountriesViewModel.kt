package com.zam.apolloandroid.ui_layer.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zam.apolloandroid.data_layer.countries.CountriesRepository
import com.zam.apolloandroid.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val repository: CountriesRepository,
) : ViewModel() {

    private var _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    fun fetchCountriesInfo(code: String) {
        viewModelScope.launch {
            repository.fetchCountries(code)
            repository.fetchCountriesInfo(code).collect { countriesInfo ->
                _countries.value = countriesInfo
            }
        }
    }

    fun starCountry(countryName: String): Unit = repository.starCountry(countryName)

    fun unStarCountry(countryName: String): Unit = repository.unStarCountry(countryName)
}