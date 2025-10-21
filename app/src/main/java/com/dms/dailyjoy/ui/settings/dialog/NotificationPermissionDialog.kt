package com.dms.dailyjoy.ui.settings.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.notification_permission_dialog_title))
        },
        text = {
            Text(text = stringResource(id = R.string.notification_permission_dialog_text))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openAppSettings(context)
                    onDismiss()
                }
            ) {
                Text(text = stringResource(id = R.string.notification_permission_dialog_settings_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dialog_close))
            }
        }
    )
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

@LightDarkPreview
@Composable
private fun NotificationPermissionDialogPreview() {
    DailyJoyTheme {
        NotificationPermissionDialog(onDismiss = {})
    }
}
