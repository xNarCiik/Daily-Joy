package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.PublicProfileDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreSearchSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : SearchSource {

    override suspend fun searchUsers(query: String, limit: Int): List<SearchResultDto> {
        if (query.isBlank()) return emptyList()
        val normalized = query.lowercase()
        val profilesCollection = firestore.collection("public_profiles")

        suspend fun runQuery(field: String): List<SearchResultDto> {
            val snapshot = profilesCollection
                .orderBy(field)
                .startAt(normalized)
                .endAt("${normalized}\uf8ff")
                .limit(limit.toLong())
                .get()
                .await()
            return snapshot.documents.mapNotNull { doc ->
                doc.toObject(PublicProfileDto::class.java)?.let { dto ->
                    SearchResultDto(doc.id, dto)
                }
            }
        }

        val handleResults = runQuery("handle")
        val usernameResults = runQuery("username")
        return (handleResults + usernameResults)
            .distinctBy { it.id }
            .take(limit)
    }
}
