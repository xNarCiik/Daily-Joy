package com.dms.flip.data.repository

import com.dms.flip.data.model.PleasureHistoryDto
import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.domain.repository.HistoryRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : HistoryRepository {

    /**
     * Lit l’historique dans l’intervalle [startDate, endDate[ (millis epoch).
     */
    override fun getHistoryForDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<PleasureHistory>> {
        val uid = auth.currentUser?.uid ?: return flowOf(emptyList())

        return callbackFlow {
            val ref = firestore.collection("users")
                .document(uid)
                .collection("history")

            val query = ref
                .whereGreaterThanOrEqualTo("dateDrawn", startDate)
                .whereLessThan("dateDrawn", endDate)
                .orderBy("dateDrawn")

            val registration = query.addSnapshotListener { snap, err ->
                if (err != null) {
                    trySend(emptyList()).isSuccess
                    return@addSnapshotListener
                }

                val list = snap?.documents?.mapNotNull { doc ->
                    val dto = doc.toObject(PleasureHistoryDto::class.java)
                    dto?.toDomain() ?: return@mapNotNull null
                }.orEmpty().sortedBy { it.dateDrawn }

                trySend(list).isSuccess
            }

            awaitClose { registration.remove() }
        }
    }
}
