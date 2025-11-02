package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.RequestDto
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
class FirestoreRequestsSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : RequestsSource {

    override fun observePendingReceived(uid: String): Flow<List<Pair<String, RequestDto>>> = callbackFlow {
        val collection = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_received")
        var registration: ListenerRegistration? = null
        val job = launch {
            registration = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot == null) return@addSnapshotListener
                val items = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(RequestDto::class.java)?.let { dto -> doc.id to dto }
                }
                trySend(items)
            }
        }
        awaitClose {
            registration?.remove()
            job.cancel()
        }
    }

    override fun observePendingSent(uid: String): Flow<List<Pair<String, RequestDto>>> = callbackFlow {
        val collection = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_sent")
        var registration: ListenerRegistration? = null
        val job = launch {
            registration = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot == null) return@addSnapshotListener
                val items = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(RequestDto::class.java)?.let { dto -> doc.id to dto }
                }
                trySend(items)
            }
        }
        awaitClose {
            registration?.remove()
            job.cancel()
        }
    }

    override suspend fun accept(uid: String, requestId: String) {
        val requestRef = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_received")
            .document(requestId)
        val snapshot = requestRef.get().await()
        if (!snapshot.exists()) return
        val dto = snapshot.toObject(RequestDto::class.java) ?: return
        val otherUserId = dto.userId
        val timestamp = System.currentTimeMillis()
        val batch = firestore.batch()
        val currentUserFriendsRef = firestore.collection("users").document(uid)
            .collection("friends").document(otherUserId)
        val otherUserFriendsRef = firestore.collection("users").document(otherUserId)
            .collection("friends").document(uid)
        batch.set(currentUserFriendsRef, mapOf("since" to timestamp))
        batch.set(otherUserFriendsRef, mapOf("since" to timestamp))
        batch.delete(requestRef)
        val otherUserRequestRef = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_sent")
            .document(requestId)
        batch.delete(otherUserRequestRef)
        val reciprocal = firestore.collection("users")
            .document(otherUserId)
            .collection("friend_requests_sent")
            .document(requestId)
        batch.delete(reciprocal)
        val reciprocalReceived = firestore.collection("users")
            .document(otherUserId)
            .collection("friend_requests_received")
            .document(requestId)
        batch.delete(reciprocalReceived)
        batch.commit().await()
    }

    override suspend fun decline(uid: String, requestId: String) {
        val requestRef = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_received")
            .document(requestId)
        val snapshot = requestRef.get().await()
        if (!snapshot.exists()) return
        val otherId = snapshot.getString("userId") ?: return
        val batch = firestore.batch()
        batch.delete(requestRef)
        val otherUserSent = firestore.collection("users")
            .document(otherId)
            .collection("friend_requests_sent")
            .document(requestId)
        batch.delete(otherUserSent)
        batch.commit().await()
    }

    override suspend fun cancelSent(uid: String, requestId: String) {
        val requestRef = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_sent")
            .document(requestId)
        val snapshot = requestRef.get().await()
        if (!snapshot.exists()) return
        val otherId = snapshot.getString("userId") ?: return
        val batch = firestore.batch()
        batch.delete(requestRef)
        val otherReceived = firestore.collection("users")
            .document(otherId)
            .collection("friend_requests_received")
            .document(requestId)
        batch.delete(otherReceived)
        batch.commit().await()
    }

    override suspend fun send(uid: String, toUserId: String): Pair<String, RequestDto> {
        val senderProfile = firestore.collection("public_profiles")
            .document(uid)
            .get()
            .await()
        val targetProfile = firestore.collection("public_profiles")
            .document(toUserId)
            .get()
            .await()
        val senderDto = RequestDto(
            userId = uid,
            username = senderProfile.getString("username") ?: "",
            handle = senderProfile.getString("handle") ?: "",
            avatarUrl = senderProfile.getString("avatar_url"),
            requestedAt = System.currentTimeMillis()
        )
        val receiverDto = RequestDto(
            userId = toUserId,
            username = targetProfile.getString("username") ?: "",
            handle = targetProfile.getString("handle") ?: "",
            avatarUrl = targetProfile.getString("avatar_url"),
            requestedAt = System.currentTimeMillis()
        )
        val currentUserSent = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_sent")
        val document = currentUserSent.document()
        val batch = firestore.batch()
        batch.set(document, receiverDto)
        val receiverCollection = firestore.collection("users")
            .document(toUserId)
            .collection("friend_requests_received")
            .document(document.id)
        batch.set(receiverCollection, senderDto)
        batch.commit().await()
        return document.id to receiverDto
    }

    override suspend fun getPendingReceivedIds(uid: String): Set<String> {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_received")
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.getString("userId") }.toSet()
    }

    override suspend fun getPendingSentIds(uid: String): Set<String> {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("friend_requests_sent")
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.getString("userId") }.toSet()
    }
}
