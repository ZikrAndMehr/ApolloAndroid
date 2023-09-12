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