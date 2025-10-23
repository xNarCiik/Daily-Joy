package com.dms.dailyjoy.ui.settings.manage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.ui.settings.manage.ManagePleasuresEvent
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasure
import com.dms.dailyjoy.ui.util.toCategoryInfo

@Composable
fun PleasuresList(
    pleasures: List<Pleasure>,
    onEvent: (ManagePleasuresEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = pleasures,
            key = { it.id }
        ) { pleasure ->
            PleasureItem(
                pleasure = pleasure,
                onToggle = { onEvent(ManagePleasuresEvent.OnPleasureToggled(pleasure)) },
                onDelete = { onEvent(ManagePleasuresEvent.OnDeletePleasureClicked(pleasure)) }
            )
        }
    }
}

@Composable
private fun PleasureItem(
    pleasure: Pleasure,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Switch compact
            Switch(
                checked = pleasure.isEnabled,
                onCheckedChange = { onToggle() },
                modifier = Modifier.scale(0.8f),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Contenu
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = pleasure.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (pleasure.isEnabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    }
                )

                CategoryBadge(
                    category = pleasure.category,
                    isEnabled = pleasure.isEnabled
                )
            }

            if (pleasure.isCustom) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_pleasure),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Divider subtil
        Spacer(modifier = Modifier.padding(top = 12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp)
                .size(height = 0.5.dp, width = 0.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        )
    }
}

@Composable
private fun CategoryBadge(
    category: PleasureCategory,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val categoryInfo = category.toCategoryInfo()

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (isEnabled) {
            categoryInfo.color.copy(alpha = 0.15f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        }
    ) {
        Text(
            text = categoryInfo.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = if (isEnabled) {
                categoryInfo.color
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            }
        )
    }
}

@LightDarkPreview
@Composable
private fun PleasureListPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PleasuresList(
                pleasures = listOf(
                    previewDailyPleasure,
                    previewDailyPleasure.copy(id = 1, isEnabled = false, isCustom = true),
                    previewDailyPleasure.copy(id = 2, title = "Un plaisir avec un très très long titre qui devrait être tronqué correctement", isEnabled = true),
                    previewDailyPleasure.copy(id = 3, isEnabled = false),
                ),
                onEvent = {}
            )
        }
    }
}
