package com.team02.xgallery.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = Firebase.auth
    private val storage = Firebase.storage

    @OptIn(ExperimentalCoroutinesApi::class)
    val authStateFlow = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            runCatching { !isClosedForSend && offer(it.currentUser) }.getOrDefault(false)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }
    val isAvailableToLogIn
        get() = auth.currentUser?.isEmailVerified == true

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun updateProfile(profileUpdates: UserProfileChangeRequest) {
        auth.currentUser!!.updateProfile(profileUpdates).await()
    }

    suspend fun sendEmailVerification() {
        auth.currentUser!!.sendEmailVerification().await()
    }

    suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    val displayName
        get() = auth.currentUser?.displayName
    val email
        get() = auth.currentUser?.email
    val userUID
        get() = auth.currentUser?.uid
    val photoUrl
        get() = auth.currentUser?.photoUrl

    suspend fun setUserAvatarFromDevice(uri: Uri) {
        val avatarPath = "$userUID/avatar/1"
        val avatarRef = storage.reference.child(avatarPath)
        avatarRef.putFile(uri).addOnSuccessListener {
            auth.currentUser?.updateProfile(
                    userProfileChangeRequest {
                        photoUri = Uri.parse(avatarPath)
                    }
            )
        }.await()
    }

    fun signOut() = auth.signOut()
}