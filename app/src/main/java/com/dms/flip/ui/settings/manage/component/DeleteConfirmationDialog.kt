package com.dms.flip.ui.settings.manage.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dms.flip.R
import com.dms.flip.data.model.Pleasure
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewDailyPleasure

@Composable
fun DeleteConfirmationDialog(
    pleasure: Pleasure?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                stringResource(R.string.manage_pleasures_delete_confirmation_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(
                    R.string.manage_pleasures_delete_confirmation_message,
                    pleasure?.title ?: ""
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.delete_pleasure),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@LightDarkPreview
@Composable
private fun DeleteConfirmationDialogPreview() {
    FlipTheme {
        DeleteConfirmationDialog(
            pleasure = previewDailyPleasure,
            onConfirm = {},
            onDismiss = {}
        )
    }
}
