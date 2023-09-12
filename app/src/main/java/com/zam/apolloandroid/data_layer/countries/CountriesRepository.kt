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