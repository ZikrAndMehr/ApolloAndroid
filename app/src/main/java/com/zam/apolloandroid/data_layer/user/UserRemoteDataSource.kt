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

package com.zam.apolloandroid.data_layer.user

import com.apollographql.apollo3.ApolloClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.zam.apolloandroid.data_layer.main.MainRemoteDataSource
import com.zam.apolloandroid.utils.AppConstants
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val apolloClient: ApolloClient
) : MainRemoteDataSource(auth, firestore, apolloClient) {

    fun checkCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun signInExistingUser(email: String, password: String): FirebaseUser? {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) continuation.resume(auth.currentUser)
                    else continuation.resume(null)
                }
        }
    }

    suspend fun signUpNewUser(email: String, password: String): FirebaseUser? {
        return suspendCoroutine {continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) continuation.resume(auth.currentUser)
                    else continuation.resume(null)
                }
        }
    }

    suspend fun saveUserData(userData: HashMap<String, String>): Boolean {
        return suspendCoroutine { continuation ->
            auth.currentUser?.let { user ->
                firestore.collection(AppConstants.FIRESTORE_MAIN_COLLECTION_PATH)
                    .document(user.uid)
                    .set(userData)
                    .addOnCompleteListener { task ->
                       continuation.resume(task.isSuccessful)
                    }
            }
        }
    }

    suspend fun sendPasswordReset(email: String): Boolean {
        return suspendCoroutine { continuation ->
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    continuation.resume(task.isSuccessful)
                }
        }
    }

    suspend fun fetchUserData(): HashMap<String, String>? {
        return suspendCoroutine { continuation ->
            auth.currentUser?.let { user ->
                firestore.collection(AppConstants.FIRESTORE_MAIN_COLLECTION_PATH)
                    .document(user.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        document?.data?.let { data ->
                            data.apply {
                                continuation.resume(
                                    hashMapOf(
                                        AppConstants.FIRESTORE_USER_EMAIL_FIELD to getValue(AppConstants.FIRESTORE_USER_EMAIL_FIELD) as String,
                                        AppConstants.FIRESTORE_USER_FIRST_NAME_FIELD to getValue(AppConstants.FIRESTORE_USER_FIRST_NAME_FIELD) as String,
                                        AppConstants.FIRESTORE_USER_LAST_NAME_FIELD to getValue(AppConstants.FIRESTORE_USER_LAST_NAME_FIELD) as String,
                                        AppConstants.FIRESTORE_USER_ADDRESS_FIELD to getValue(AppConstants.FIRESTORE_USER_ADDRESS_FIELD) as String,
                                        AppConstants.FIRESTORE_USER_DATE_OF_BIRTH_FIELD to getValue(AppConstants.FIRESTORE_USER_DATE_OF_BIRTH_FIELD) as String
                                    )
                                )
                            }
                        } ?: continuation.resume(null)
                    }
            }
        }
    }

    fun signOutUser() {
        auth.signOut()
    }
}