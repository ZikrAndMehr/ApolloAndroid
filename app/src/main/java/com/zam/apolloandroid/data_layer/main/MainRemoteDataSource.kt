package com.zam.apolloandroid.data_layer.main

import com.apollographql.apollo3.ApolloClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class MainRemoteDataSource(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val apolloClient: ApolloClient
)