package com.dms.flip.domain.usecase.storage

import android.net.Uri
import com.dms.flip.data.repository.StorageRepository
import javax.inject.Inject

class UploadAvatarUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(imageUri: Uri): String {
        return storageRepository.uploadUserAvatar(imageUri)
    }
}
