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

import javax.inject.Inject

class UserRepository @Inject constructor(private val remoteDataSource: UserRemoteDataSource) {

    fun checkCurrentUser() = remoteDataSource.checkCurrentUser()

    suspend fun signInExistingUser(email: String, password: String) = remoteDataSource.signInExistingUser(email, password)

    suspend fun signUpNewUser(email: String, password: String) = remoteDataSource.signUpNewUser(email, password)

    suspend fun saveUserData(userData: HashMap<String, String>) = remoteDataSource.saveUserData(userData)

    suspend fun sendPasswordReset(email: String) = remoteDataSource.sendPasswordReset(email)

    suspend fun fetchUserData() = remoteDataSource.fetchUserData()

    fun signOutUser() = remoteDataSource.signOutUser()
}