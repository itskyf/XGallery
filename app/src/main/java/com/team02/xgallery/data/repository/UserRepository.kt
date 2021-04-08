package com.team02.xgallery.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = Firebase.auth

    @OptIn(ExperimentalCoroutinesApi::class)
    val authStateFlow = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            runCatching { !isClosedForSend && offer(it.currentUser) }.getOrDefault(false)
            // TODO
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    val isAvailableToLogIn
        get() = auth.currentUser?.isEmailVerified == true

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    fun createUserWithEmailAndPassword(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)

    fun updateProfile(profileUpdates: UserProfileChangeRequest) =
        auth.currentUser!!.updateProfile(profileUpdates)

    fun sendEmailVerification() =
        auth.currentUser!!.sendEmailVerification()

    fun sendPasswordResetEmail(email: String) =
        auth.sendPasswordResetEmail(email)

    fun getDisplayName(): String {
        return auth.currentUser.displayName
    }

    fun getEmail(): String {
        return auth.currentUser.email
    }

    fun getAvatar(): Uri {
        return auth.currentUser.photoUrl
    }

    fun signOut() {
        auth.signOut()
    }
}


