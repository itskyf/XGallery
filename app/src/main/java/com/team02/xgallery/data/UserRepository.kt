package com.team02.xgallery.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository() {
    private val auth = Firebase.auth

    val authStateChanged: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener {
                offer(it.currentUser)
                // TODO
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    suspend fun signInWithEmailAndPassword(email: String, password: String) =
            auth.signInWithEmailAndPassword(email, password)

    suspend fun createUserWithEmailAndPassword(email: String, password: String) =
            auth.createUserWithEmailAndPassword(email, password)
}