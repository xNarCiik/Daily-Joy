package com.dms.flip.ui.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FileProviderHelper {

    /**
     * Crée un fichier temporaire pour la photo prise avec la caméra
     * et retourne son URI via FileProvider
     */
    fun createImageUri(context: Context): Uri {
        val directory = File(context.cacheDir, "images")
        directory.mkdirs()

        val file = File.createTempFile(
            System.currentTimeMillis().toString(),
            ".jpg",
            directory
        )

        val authority = "${context.packageName}.fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }
}
