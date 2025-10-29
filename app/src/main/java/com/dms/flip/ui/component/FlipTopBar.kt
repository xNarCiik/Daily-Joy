package com.dms.flip.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.flip.R
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview

data class TopBarIcon(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit
)

@Composable
fun FlipTopBar(
    modifier: Modifier = Modifier,
    title: String,
    startTopBarIcon: TopBarIcon? = null,
    endTopBarIcon: TopBarIcon? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bouton Start
            IconButton(
                onClick = { startTopBarIcon?.onClick() },
                modifier = Modifier.size(48.dp),
                enabled = startTopBarIcon != null
            ) {
                startTopBarIcon?.let {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.contentDescription,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Titre centré
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Bouton End
            IconButton(
                onClick = { endTopBarIcon?.onClick() },
                modifier = Modifier.size(48.dp),
                enabled = endTopBarIcon != null
            ) {
                endTopBarIcon?.let {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.contentDescription,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

// ========== PREVIEW ==========
@LightDarkPreview
@Composable
private fun FlipTopBarPreview() {
    FlipTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FlipTopBar(
                title = "Flip",
                startTopBarIcon = TopBarIcon(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.settings_back),
                    onClick = { }
                ),
                endTopBarIcon = TopBarIcon(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.settings_back),
                    onClick = {}
                )
            )
        }
    }
}
