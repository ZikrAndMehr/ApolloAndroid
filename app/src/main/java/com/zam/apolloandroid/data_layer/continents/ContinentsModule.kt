package com.zam.apolloandroid.data_layer.continents

import com.apollographql.apollo3.ApolloClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContinentsModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        apolloClient: ApolloClient
    ) = ContinentsRemoteDataSource(auth, firestore, apolloClient)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: ContinentsRemoteDataSource) = ContinentsRepository(remoteDataSource)
}