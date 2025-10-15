package com.dms.dailyjoy.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dms.dailyjoy.ui.PleasureViewModel
import com.dms.dailyjoy.ui.component.PleasureCard
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@Composable
fun HomeScreen() {
    val viewModel: PleasureViewModel = hiltViewModel()
    val pleasures by viewModel.pleasures.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            items(pleasures) { pleasure ->
                PleasureCard(pleasure = pleasure)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DailyJoyTheme {
        HomeScreen()
    }
}