package com.zam.apolloandroid.data_layer.continents

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zam.apolloandroid.data_layer.main.MainRemoteDataSource
import com.zam.apolloandroid.utils.ApolloContinent
import countries.GetContinentsQuery
import javax.inject.Inject

class ContinentsRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val apolloClient: ApolloClient
) : MainRemoteDataSource(auth, firestore, apolloClient) {

    suspend fun fetchContinents(): List<ApolloContinent> {
        val response = try {
            apolloClient.query(GetContinentsQuery()).execute()
        } catch (e: ApolloException) {
            Log.d("ContinentsRemoteDataSource", "Failure", e)
            null
        }

        return response?.data?.continents ?: emptyList()
    }
}