package com.team02.xgallery.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository {
    private val auth = FirebaseAuth.getInstance()

    val authStateChanged: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener {
                offer(it.currentUser)
                // TODO
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    fun isSignedIn() =
            auth.currentUser != null

    fun isEmailVerified() =
            auth.currentUser!!.isEmailVerified

    fun signInWithEmailAndPassword(email: String, password: String) =
            auth.signInWithEmailAndPassword(email, password)

    fun createUserWithEmailAndPassword(email: String, password: String) =
            auth.createUserWithEmailAndPassword(email, password)

    fun updateProfile(profileUpdates: UserProfileChangeRequest) =
            auth.currentUser!!.updateProfile(profileUpdates)

    fun sendEmailVerification() =
            auth.currentUser!!.sendEmailVerification(null)
}