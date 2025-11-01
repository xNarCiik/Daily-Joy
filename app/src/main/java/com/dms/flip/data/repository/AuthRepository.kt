package com.dms.flip.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser).isSuccess
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun deleteAccount() {
        val user = auth.currentUser ?: return

        // TODO MIGRATE TO CREDENTIAL MANAGER
        // https://developer.android.com/identity/sign-in/legacy-gsi-migration?hl=fr
        try {
            user.delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

}
