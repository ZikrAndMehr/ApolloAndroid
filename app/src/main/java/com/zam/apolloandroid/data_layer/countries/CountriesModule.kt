package com.zam.apolloandroid.data_layer.countries

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
object CountriesModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        apolloClient: ApolloClient
    ) = CountriesRemoteDataSource(auth, firestore, apolloClient)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: CountriesRemoteDataSource) = CountriesRepository(remoteDataSource)
}