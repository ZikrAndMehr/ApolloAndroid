package com.zam.apolloandroid.data_layer.continents

import javax.inject.Inject

class ContinentsRepository @Inject constructor(private val remoteDataSource: ContinentsRemoteDataSource) {

    suspend fun fetchContinents() = remoteDataSource.fetchContinents()
}