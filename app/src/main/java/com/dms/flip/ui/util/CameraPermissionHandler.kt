package com.dms.flip.ui.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CameraPermissionHandler {
    
    /**
     * Vérifie si la permission caméra est accordée
     */
    fun isPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Vérifie si l'utilisateur a refusé la permission de manière permanente
     * (coché "Ne plus demander")
     */
    fun isPermanentlyDenied(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            !ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) && !isPermissionGranted(activity)
        } else {
            false
        }
    }
    
    /**
     * Vérifie si on doit afficher une explication à l'utilisateur
     * (avant de demander la permission)
     */
    fun shouldShowRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.CAMERA
        )
    }
}
