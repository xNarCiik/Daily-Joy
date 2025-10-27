package com.dms.dailyjoy.ui.settings.manage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.component.ErrorState
import com.dms.dailyjoy.ui.component.LoadingState
import com.dms.dailyjoy.ui.settings.manage.component.AddPleasureBottomSheet
import com.dms.dailyjoy.ui.settings.manage.component.CategoryFilters
import com.dms.dailyjoy.ui.settings.manage.component.DeleteConfirmationDialog
import com.dms.dailyjoy.ui.settings.manage.component.ManagePleasuresEmptyState
import com.dms.dailyjoy.ui.settings.manage.component.ManagePleasuresList
import com.dms.dailyjoy.ui.settings.manage.component.TypeFilterDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePleasuresScreen(
    uiState: ManagePleasuresUiState,
    onEvent: (ManagePleasuresEvent) -> Unit,
    navigateBack: () -> Unit
) {
    var showTypeFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.manage_pleasures_title),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.selectedTab.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showTypeFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.manage_pleasures_filter_by_type_description)
                        )
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
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_pleasure)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            CategoryFilters(
                selectedCategories = uiState.selectedCategories,
                onCategorySelected = { onEvent(ManagePleasuresEvent.OnCategoryFilterChanged(it)) }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        LoadingState(modifier = Modifier.fillMaxSize())
                    }

                    uiState.error != null -> {
                        ErrorState(
                            message = uiState.error,
                            onRetry = { onEvent(ManagePleasuresEvent.OnRetryClicked) }
                        )
                    }

                    uiState.filteredPleasures.isEmpty() -> {
                        ManagePleasuresEmptyState(type = uiState.selectedTab)
                    }

                    else -> {
                        ManagePleasuresList(
                            pleasures = uiState.filteredPleasures,
                            onEvent = onEvent
                        )
                    }
                }
            }
        }

        if (showTypeFilterDialog) {
            TypeFilterDialog(
                selectedTab = uiState.selectedTab,
                onTabSelected = { onEvent(ManagePleasuresEvent.OnTabSelected(it)) },
                onDismiss = { showTypeFilterDialog = false }
            )
        }

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

        if (uiState.showDeleteConfirmation) {
            DeleteConfirmationDialog(
                pleasure = uiState.pleasureToDelete,
                onConfirm = { onEvent(ManagePleasuresEvent.OnDeleteConfirmed) },
                onDismiss = { onEvent(ManagePleasuresEvent.OnDeleteCancelled) }
            )
        }
    }
}
