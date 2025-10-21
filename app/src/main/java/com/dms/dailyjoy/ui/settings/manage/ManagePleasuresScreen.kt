package com.dms.dailyjoy.ui.settings.manage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePleasuresScreen(
    uiState: ManagePleasuresUiState,
    onEvent: (ManagePleasuresEvent) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Gérer mes plaisirs",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !uiState.showAddDialog,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { onEvent(ManagePleasuresEvent.OnAddPleasureClicked) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter un plaisir")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Tabs
                PleasureTypeTabs(
                    selectedTab = uiState.selectedTab,
                    onTabSelected = { onEvent(ManagePleasuresEvent.OnTabSelected(it)) }
                )

                // Content
                when {
                    uiState.isLoading -> {
                        LoadingState()
                    }
                    uiState.error != null -> {
                        ErrorState(
                            message = uiState.error,
                            onRetry = { onEvent(ManagePleasuresEvent.OnRetryClicked) }
                        )
                    }
                    uiState.filteredPleasures.isEmpty() -> {
                        EmptyState(type = uiState.selectedTab)
                    }
                    else -> {
                        PleasuresList(
                            pleasures = uiState.filteredPleasures,
                            onPleasureToggled = { onEvent(ManagePleasuresEvent.OnPleasureToggled(it)) },
                            onPleasureDelete = { onEvent(ManagePleasuresEvent.OnDeletePleasureClicked(it)) }
                        )
                    }
                }
            }

            // Add pleasure bottom sheet
            if (uiState.showAddDialog) {
                AddPleasureBottomSheet(
                    title = uiState.newPleasureTitle,
                    description = uiState.newPleasureDescription,
                    category = uiState.newPleasureCategory,
                    titleError = uiState.titleError,
                    descriptionError = uiState.descriptionError,
                    onDismiss = { onEvent(ManagePleasuresEvent.OnBottomSheetDismissed) },
                    onTitleChange = { onEvent(ManagePleasuresEvent.OnTitleChanged(it)) },
                    onDescriptionChange = { onEvent(ManagePleasuresEvent.OnDescriptionChanged(it)) },
                    onCategoryChange = { onEvent(ManagePleasuresEvent.OnCategoryChanged(it)) },
                    onSave = { onEvent(ManagePleasuresEvent.OnSavePleasureClicked) }
                )
            }

            // Delete confirmation dialog
            if (uiState.showDeleteConfirmation) {
                DeleteConfirmationDialog(
                    pleasure = uiState.pleasureToDelete,
                    onConfirm = { onEvent(ManagePleasuresEvent.OnDeleteConfirmed) },
                    onDismiss = { onEvent(ManagePleasuresEvent.OnDeleteCancelled) }
                )
            }
        }
    }
}

@Composable
private fun PleasureTypeTabs(
    selectedTab: ManagePleasuresTab,
    onTabSelected: (ManagePleasuresTab) -> Unit
) {
    TabRow(
        selectedTabIndex = if (selectedTab == ManagePleasuresTab.SMALL) 0 else 1,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[if (selectedTab == ManagePleasuresTab.SMALL) 0 else 1]),
                color = MaterialTheme.colorScheme.primary,
                height = 3.dp
            )
        }
    ) {
        Tab(
            selected = selectedTab == ManagePleasuresTab.SMALL,
            onClick = { onTabSelected(ManagePleasuresTab.SMALL) },
            text = {
                Text(
                    "Petits Plaisirs",
                    fontWeight = if (selectedTab == ManagePleasuresTab.SMALL) FontWeight.Bold else FontWeight.Normal
                )
            }
        )
        Tab(
            selected = selectedTab == ManagePleasuresTab.BIG,
            onClick = { onTabSelected(ManagePleasuresTab.BIG) },
            text = {
                Text(
                    "Gros Plaisirs",
                    fontWeight = if (selectedTab == ManagePleasuresTab.BIG) FontWeight.Bold else FontWeight.Normal
                )
            }
        )
    }
}

@Composable
private fun PleasuresList(
    pleasures: List<Pleasure>,
    onPleasureToggled: (Pleasure) -> Unit,
    onPleasureDelete: (Pleasure) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            items = pleasures,
            key = { _, pleasure -> pleasure.id }
        ) { index, pleasure ->
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(index * 50L)
                isVisible = true
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically()
            ) {
                PleasureItem(
                    pleasure = pleasure,
                    onToggle = { onPleasureToggled(pleasure) },
                    onDelete = { onPleasureDelete(pleasure) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PleasureItem(
    pleasure: Pleasure,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (pleasure.isEnabled) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (pleasure.isEnabled) 4.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (pleasure.isEnabled) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Toggle switch
            Switch(
                checked = pleasure.isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(
                modifier = Modifier.weight(1f)
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
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    }
                )

                if (pleasure.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pleasure.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (pleasure.isEnabled) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        }
                    )
                }

                // Category badge
                Spacer(modifier = Modifier.height(8.dp))
                CategoryBadge(category = pleasure.category)
            }

            // Delete button (only for custom pleasures)
            if (pleasure.isCustom) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBadge(category: PleasureCategory) {
    val categoryInfo = category.toCategoryInfo()

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = categoryInfo.color.copy(alpha = 0.2f),
        contentColor = categoryInfo.color
    ) {
        Text(
            text = categoryInfo.label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPleasureBottomSheet(
    title: String,
    description: String,
    category: PleasureCategory,
    titleError: String?,
    descriptionError: String?,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (PleasureCategory) -> Unit,
    onSave: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Ajouter un plaisir",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Titre") },
                placeholder = { Text("Ex: Boire un bon café") },
                isError = titleError != null,
                supportingText = titleError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                placeholder = { Text("Décrivez ce plaisir...") },
                isError = descriptionError != null,
                supportingText = descriptionError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = getCategoryLabel(category),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Catégorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    PleasureCategory.entries.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(getCategoryLabel(cat)) },
                            onClick = {
                                onCategoryChange(cat)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save button
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sauvegarder", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    pleasure: Pleasure?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Warning, contentDescription = null) },
        title = { Text("Supprimer le plaisir ?") },
        text = {
            Text("Êtes-vous sûr de vouloir supprimer \"${pleasure?.title}\" ? Cette action est irréversible.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Supprimer", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Une erreur est survenue",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Réessayer")
        }
    }
}

@Composable
private fun EmptyState(type: ManagePleasuresTab) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(20.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Aucun plaisir",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when (type) {
                ManagePleasuresTab.SMALL -> "Ajoutez vos petits plaisirs quotidiens"
                ManagePleasuresTab.BIG -> "Ajoutez vos grandes récompenses"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun getCategoryLabel(category: PleasureCategory): String {
    return when (category) {
        PleasureCategory.FOOD -> "Nourriture"
        PleasureCategory.ENTERTAINMENT -> "Divertissement"
        PleasureCategory.SOCIAL -> "Social"
        PleasureCategory.WELLNESS -> "Bien-être"
        PleasureCategory.CREATIVE -> "Créatif"
        PleasureCategory.OUTDOOR -> "Extérieur"
        PleasureCategory.OTHER -> "Autre"
        else -> "Autre" // TODO
    }
}
