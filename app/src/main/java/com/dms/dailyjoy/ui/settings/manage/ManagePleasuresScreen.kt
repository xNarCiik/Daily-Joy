package com.dms.dailyjoy.ui.settings.manage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManagePleasuresScreen(
    viewModel: ManagePleasuresViewModel = hiltViewModel()
) {
    val pleasures by viewModel.pleasures.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_pleasure))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val tabTitles = listOf(
                stringResource(R.string.small_pleasures),
                stringResource(R.string.big_pleasures)
            )

            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) }
                    )
                }
            }

            Box(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.pleasures_explanation),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            HorizontalPager(state = pagerState) { page ->
                val pleasuresToShow = when (page) {
                    0 -> pleasures.filter { p -> p.type == PleasureType.SMALL }
                    1 -> pleasures.filter { p -> p.type == PleasureType.BIG }
                    else -> emptyList()
                }
                PleasuresList(pleasures = pleasuresToShow, viewModel = viewModel)
            }
        }
    }

    if (showAddDialog) {
        AddPleasureDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, description, category ->
                val type = if (pagerState.currentPage == 0) PleasureType.SMALL else PleasureType.BIG
                viewModel.addPleasure(title, description, category, type)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun PleasuresList(pleasures: List<Pleasure>, viewModel: ManagePleasuresViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(pleasures) { pleasure ->
            PleasureManagementItem(
                pleasure = pleasure,
                onToggle = { isEnabled -> viewModel.updatePleasure(pleasure.copy(isEnabled = isEnabled)) },
                onDelete = { viewModel.deletePleasure(pleasure) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PleasureManagementItem(
    pleasure: Pleasure,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = { Text(pleasure.title) },
        leadingContent = {
            Switch(
                checked = pleasure.isEnabled,
                onCheckedChange = onToggle
            )
        },
        trailingContent = {
            if (pleasure.isCustom) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete_pleasure)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPleasureDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, category: PleasureCategory) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(PleasureCategory.FOOD) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_pleasure)) },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.title)) },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedCategory.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        PleasureCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, description, selectedCategory) },
                enabled = title.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
