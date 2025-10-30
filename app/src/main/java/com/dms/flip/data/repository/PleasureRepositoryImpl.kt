package com.dms.flip.data.repository

import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.data.model.PleasureDto
import com.dms.flip.data.model.PleasureHistoryDto
import com.dms.flip.data.model.toDto
import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.domain.repository.PleasureRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PleasureRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : PleasureRepository {

    private val userId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    override fun getPleasures(): Flow<List<Pleasure>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val collectionRef = firestore
            .collection("users")
            .document(userId)
            .collection("pleasures")

        val listener = collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }

            val pleasures = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(PleasureDto::class.java)?.toDomain(doc.id)
            }.orEmpty()

            trySend(pleasures)
        }

        awaitClose { listener.remove() }
    }

    override fun getPleasuresCount(): Flow<Int> {
        return getPleasures().map { pleasures ->
            pleasures.count { it.isEnabled }
        }
    }

    override fun getRandomPleasure(category: PleasureCategory?): Flow<Pleasure> {
        return getPleasures().map { pleasures ->
            val filteredList =
                pleasures.filter { it.isEnabled && (category == PleasureCategory.ALL || category == null || it.category == category) }
            if (filteredList.isEmpty()) {
                throw IllegalStateException("No pleasures available for this category.")
            }
            filteredList.random()
        }
    }

    override suspend fun insert(pleasure: Pleasure) {
        firestore.collection("users").document(userId).collection("pleasures")
            .add(pleasure.toDto()).await()
    }

    override suspend fun update(pleasure: Pleasure) {
        firestore.collection("users").document(userId).collection("pleasures")
            .document(pleasure.id).set(pleasure.toDto()).await()
    }

    override suspend fun delete(pleasuresId: List<String>) {
        val batch = firestore.batch()
        pleasuresId.forEach { pleasureId ->
            val docRef = firestore.collection("users").document(userId).collection("pleasures").document(pleasureId)
            batch.delete(docRef)
        }
        batch.commit().await()
    }

    override suspend fun upsertPleasureHistory(pleasureHistory: PleasureHistory) {
        firestore.collection("users").document(userId).collection("history")
            .document(pleasureHistory.dayIdentifier).set(pleasureHistory.toDto()).await()
    }

    override suspend fun getPleasureHistory(dayIdentifier: String): PleasureHistory? {
        val document = firestore.collection("users").document(userId).collection("history")
            .document(dayIdentifier).get().await()

        return document.toObject(PleasureHistoryDto::class.java)?.toDomain(document.id)
    }
}
