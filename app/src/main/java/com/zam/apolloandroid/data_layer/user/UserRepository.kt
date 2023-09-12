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