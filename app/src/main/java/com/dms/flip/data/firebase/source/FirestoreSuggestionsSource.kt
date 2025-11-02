package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.SuggestionDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreSuggestionsSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : SuggestionsSource {

    override fun observeSuggestions(uid: String): Flow<List<Pair<String, SuggestionDto>>> = callbackFlow {
        val collection = firestore.collection("users")
            .document(uid)
            .collection("suggestions")
        var registration: ListenerRegistration? = null
        val job = launch {
            registration = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot == null) return@addSnapshotListener
                val suggestions = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(SuggestionDto::class.java)?.let { dto -> doc.id to dto }
                }
                trySend(suggestions)
            }
        }
        awaitClose {
            registration?.remove()
            job.cancel()
        }
    }

    override suspend fun hideSuggestion(uid: String, userId: String) {
        firestore.collection("users")
            .document(uid)
            .collection("suggestions")
            .document(userId)
            .delete()
            .await()
    }
}
