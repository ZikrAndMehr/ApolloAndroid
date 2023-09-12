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

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.zam.apolloandroid.data_layer.main.MainRemoteDataSource
import com.zam.apolloandroid.model.Country
import com.zam.apolloandroid.utils.ApolloCountry
import com.zam.apolloandroid.utils.AppConstants
import countries.GetCountriesOfContinentQuery
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.internal.format
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CountriesRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val apolloClient: ApolloClient
) : MainRemoteDataSource(auth, firestore, apolloClient) {

    private lateinit var code: String
    private var countriesCount = 0

    suspend fun fetchCountries(code: String) {
        this.code = code
        val response = try {
            apolloClient.query(GetCountriesOfContinentQuery(code)).execute()
        } catch (e: ApolloException) {
            Log.d("CountriesRemoteDataSource", "Failure", e)
            null
        }
        val countries = response?.data?.continent?.countries as List<ApolloCountry>
        countriesCount = countries.size

        if (fetchCountriesCount(code) == 1) createCountriesDocument(code, countries)
    }

    private fun createCountriesDocument(code: String, countries: List<ApolloCountry>) {
        val data = hashMapOf(
            AppConstants.FIRESTORE_COUNTRY_EMOJI_FIELD to "",
            AppConstants.FIRESTORE_COUNTRY_NAME_FIELD to "",
            AppConstants.FIRESTORE_COUNTRY_NATIVE_NAME_FIELD to "",
            AppConstants.FIRESTORE_COUNTRY_STARRED_FIELD to false
        )
        val countriesRef = firestore.collection(
            auth.currentUser?.uid?.let { uid ->
                format(AppConstants.FIRESTORE_COUNTRIES_COLLECTION_PATH, uid, code)
            }.toString()
        )

        firestore.runBatch { batch ->
            countries.forEach { country ->
                data[AppConstants.FIRESTORE_COUNTRY_EMOJI_FIELD] = country.emoji
                data[AppConstants.FIRESTORE_COUNTRY_NAME_FIELD] = country.name
                data[AppConstants.FIRESTORE_COUNTRY_NATIVE_NAME_FIELD] = country.native
                batch.set(countriesRef.document(country.name), data)
            }
        }
    }

    suspend fun fetchCountriesInfo(code: String): Flow<List<Country>> = callbackFlow {
        auth.currentUser?.let { user ->
            var countriesCollection: CollectionReference? = null
            try {
                countriesCollection = firestore.collection(format(AppConstants.FIRESTORE_COUNTRIES_COLLECTION_PATH, user.uid, code))
            } catch (e: Throwable) {
                close(e)
            }

            val subscription = countriesCollection?.addSnapshotListener { snapshot, error ->
                if (snapshot == null) return@addSnapshotListener
                try {
                    trySend(snapshot.toObjects(Country::class.java) as List<Country>)
                } catch (e: Throwable) {
                    close(e)
                }
            }

            awaitClose{ subscription?.remove() }
        }
    }

    suspend fun fetchCountriesCount(code: String): Int {
        val count = 1
        return suspendCoroutine { continuation ->
            auth.currentUser?.let { user ->
                firestore.collection(format(AppConstants.FIRESTORE_COUNTRIES_COLLECTION_PATH, user.uid, code))
                    .get()
                    .addOnSuccessListener { countries ->
                        continuation.resume(
                            if (0 < countries.size()) countries.size()
                            else 1
                        )
                    }
                    .addOnFailureListener { continuation.resume(count) }
            } ?: continuation.resume(count)
        }
    }

    suspend fun fetchStarredCountriesCount(code: String): Int {
        val count = 0
        return suspendCoroutine { continuation ->
            auth.currentUser?.let { user ->
                firestore.collection(format(AppConstants.FIRESTORE_COUNTRIES_COLLECTION_PATH, user.uid, code))
                    .whereEqualTo(AppConstants.FIRESTORE_COUNTRY_STARRED_FIELD, true)
                    .get()
                    .addOnSuccessListener { countries ->
                        continuation.resume(countries.size())
                    }
                    .addOnFailureListener { continuation.resume(count) }
            } ?: continuation.resume(count)
        }
    }

    fun starCountry(countryName: String) {
        auth.currentUser?.let { user ->
            firestore.collection(format(AppConstants.FIRESTORE_COUNTRIES_COLLECTION_PATH, user.uid, code))
                .document(countryName)
                .update(AppConstants.FIRESTORE_COUNTRY_STARRED_FIELD, true)
        }
    }

    fun unStarCountry(countryName: String) {
        auth.currentUser?.let { user ->
            firestore.collection(format(AppConstants.FIRESTORE_COUNTRIES_COLLECTION_PATH, user.uid, code))
                .document(countryName)
                .update(AppConstants.FIRESTORE_COUNTRY_STARRED_FIELD, false)
        }
    }
}