package com.dms.dailyjoy.ui.settings.manage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManagePleasuresScreen(
    viewModel: ManagePleasuresViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val pleasures by viewModel.pleasures.collectAsState()
    var showAddPleasureSheet by remember { mutableStateOf(false) }
    var isDeleteMode by remember { mutableStateOf(false) }
    var selectedPleasures by remember { mutableStateOf(emptySet<Pleasure>()) }

    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ManagePleasuresTopAppBar(
                isDeleteMode = isDeleteMode,
                selectedCount = selectedPleasures.size,
                onDeleteModeChange = { isDeleteMode = it },
                onClearSelection = { selectedPleasures = emptySet() },
                onDeleteSelected = {
                    // TODO viewModel.deletePleasures(selectedPleasures.toList())
                    selectedPleasures = emptySet()
                    isDeleteMode = false
                },
                navigateBack = navigateBack
            )
        },
        floatingActionButton = {
            if (!isDeleteMode) {
                FloatingActionButton(onClick = { showAddPleasureSheet = true }) {
                    Icon(
                        Icons.Filled.Add,
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

            HorizontalPager(state = pagerState) { page ->
                val pleasuresToShow = when (page) {
                    0 -> pleasures.filter { p -> p.type == PleasureType.SMALL }
                    1 -> pleasures.filter { p -> p.type == PleasureType.BIG }
                    else -> emptyList()
                }
                PleasuresList(
                    pleasures = pleasuresToShow,
                    viewModel = viewModel,
                    isDeleteMode = isDeleteMode,
                    selectedPleasures = selectedPleasures,
                    onPleasureSelected = { pleasure, isSelected ->
                        selectedPleasures = if (isSelected) {
                            selectedPleasures + pleasure
                        } else {
                            selectedPleasures - pleasure
                        }
                    }
                )
            }
        }
    }

    if (showAddPleasureSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showAddPleasureSheet = false },
            sheetState = sheetState
        ) {
            AddPleasureSheet(
                onSave = { title, description, category ->
                    val type =
                        if (pagerState.currentPage == 0) PleasureType.SMALL else PleasureType.BIG
                    viewModel.addPleasure(title, description, category, type)
                    scope.launch {
                        sheetState.hide()
                        showAddPleasureSheet = false
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManagePleasuresTopAppBar(
    isDeleteMode: Boolean,
    selectedCount: Int,
    onDeleteModeChange: (Boolean) -> Unit,
    onClearSelection: () -> Unit,
    onDeleteSelected: () -> Unit,
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = { if (isDeleteMode) Text("$selectedCount selected") else Text(stringResource(R.string.manage_pleasures_title)) },
        navigationIcon = {
            if (isDeleteMode) {
                IconButton(onClick = {
                    onDeleteModeChange(false)
                    onClearSelection()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.cancel)
                    )
                }
            } else {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (isDeleteMode) {
                IconButton(onClick = onDeleteSelected, enabled = selectedCount > 0) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete") // TODO
                }
            } else {
                IconButton(onClick = { onDeleteModeChange(true) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete pleasure" // TODO
                    )
                }
            }
        }
    )
}


@Composable
private fun PleasuresList(
    pleasures: List<Pleasure>,
    viewModel: ManagePleasuresViewModel,
    isDeleteMode: Boolean,
    selectedPleasures: Set<Pleasure>,
    onPleasureSelected: (Pleasure, Boolean) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(pleasures) { pleasure ->
            PleasureManagementItem(
                pleasure = pleasure,
                isDeleteMode = isDeleteMode,
                isSelected = selectedPleasures.contains(pleasure),
                onToggle = { isEnabled -> viewModel.updatePleasure(pleasure.copy(isEnabled = isEnabled)) },
                onSelect = { onPleasureSelected(pleasure, it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PleasureManagementItem(
    pleasure: Pleasure,
    isDeleteMode: Boolean,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    onSelect: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(pleasure.title) },
        leadingContent = {
            if (isDeleteMode) {
                Checkbox(checked = isSelected, onCheckedChange = onSelect)
            } else {
                Switch(
                    checked = pleasure.isEnabled,
                    onCheckedChange = onToggle
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPleasureSheet(
    onSave: (title: String, description: String, category: PleasureCategory) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(PleasureCategory.FOOD) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(stringResource(R.string.add_pleasure), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSave(title, description, selectedCategory) },
            enabled = title.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }
    }
}
