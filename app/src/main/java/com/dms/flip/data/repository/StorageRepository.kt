package com.dms.flip.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) {
    suspend fun uploadUserAvatar(imageUri: Uri): String {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("Utilisateur non connecté")

        val storageRef = storage.reference.child("avatars/$userId/avatar.jpg")

        // Upload du fichier
        storageRef.putFile(imageUri).await()

        // Récupération du lien de téléchargement
        val downloadUrl = storageRef.downloadUrl.await().toString()

        // Mise à jour du Firestore
        firestore.collection("users").document(userId)
            .update("avatar_url", downloadUrl).await()

        return downloadUrl
    }
}
