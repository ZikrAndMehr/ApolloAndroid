package com.zam.apolloandroid.data_layer.countries

import javax.inject.Inject

class CountriesRepository @Inject constructor(private val remoteDataSource: CountriesRemoteDataSource) {

    suspend fun fetchCountries(code: String) = remoteDataSource.fetchCountries(code)

    suspend fun fetchCountriesInfo(code: String) = remoteDataSource.fetchCountriesInfo(code)

    suspend fun fetchCountriesCount(code: String) = remoteDataSource.fetchCountriesCount(code)

    suspend fun fetchStarredCountriesCount(code: String) = remoteDataSource.fetchStarredCountriesCount(code)

    fun unStarCountry(countryName: String) = remoteDataSource.unStarCountry(countryName)

    fun starCountry(countryName: String) = remoteDataSource.starCountry(countryName)
}