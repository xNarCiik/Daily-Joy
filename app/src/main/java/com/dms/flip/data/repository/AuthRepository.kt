package com.dms.flip.data.repository

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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

    suspend fun deleteAccount(context: Context) {
        val user = auth.currentUser ?: return

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("251003841383-uiaqo6aq7himht7bt3d78aeaqga456h3.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

        val googleAccount: GoogleSignInAccount? =
            GoogleSignIn.getLastSignedInAccount(context)
                ?: googleSignInClient.silentSignIn().await()

        if (googleAccount == null || googleAccount.idToken == null) {
            throw Exception("Impossible d'obtenir un token Google valide pour la réauthentification.")
        }

        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)

        user.reauthenticate(credential).await()
        user.delete().await()
    }

}
