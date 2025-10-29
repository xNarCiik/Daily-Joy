package com.dms.flip.data.repository

import com.dms.flip.data.database.mapper.toFirestoreEntity
import com.dms.flip.data.model.Pleasure
import com.dms.flip.domain.repository.onboarding.OnboardingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OnboardingRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : OnboardingRepository {

    override fun getOnboardingStatus(userId: String): Flow<Boolean> = callbackFlow {
        val docRef = firestore.collection("users").document(userId)
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Envoyer false en cas d'erreur ou si le doc n'existe pas
                trySend(false).isSuccess
                close(error)
                return@addSnapshotListener
            }

            val isCompleted = snapshot?.getBoolean("onboarding_completed") ?: false
            trySend(isCompleted).isSuccess
        }
        awaitClose { listener.remove() }
    }

    override suspend fun saveOnboardingStatus(username: String, pleasures: List<Pleasure>) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userDoc = firestore.collection("users").document(userId)
        userDoc.set(mapOf("username" to username, "onboarding_completed" to true)).await()

        val pleasuresCollection =
            firestore.collection("users").document(userId).collection("pleasures")
        pleasures.forEach { pleasure ->
            pleasuresCollection.add(pleasure.toFirestoreEntity()).await()
        }
    }
}
