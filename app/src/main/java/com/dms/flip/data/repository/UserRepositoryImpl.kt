package com.dms.flip.data.repository

import com.dms.flip.domain.model.UserInfo
import com.dms.flip.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    override fun getUserInfo(): Flow<UserInfo?> = callbackFlow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val uid = currentUser.uid
        val email = currentUser.email

        val registration = firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }

                val userInfo = snapshot?.takeIf { it.exists() }?.let {
                    UserInfo(
                        id = uid,
                        username = it.getString("username"),
                        email = email,
                        avatarUrl = it.getString("avatar_url")
                    )
                }

                trySend(userInfo)
            }

        awaitClose { registration.remove() }
    }.distinctUntilChanged { old, new -> old == new }
}
