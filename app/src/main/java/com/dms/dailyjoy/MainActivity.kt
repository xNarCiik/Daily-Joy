@file:OptIn(ExperimentalMaterial3Api::class)

package com.dms.dailyjoy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.ui.HistoryRoute
import com.dms.dailyjoy.ui.HomeRoute
import com.dms.dailyjoy.ui.SettingsRoute
import com.dms.dailyjoy.ui.components.BottomNavBar
import com.dms.dailyjoy.ui.history.HistoryScreen
import com.dms.dailyjoy.ui.home.HomeScreen
import com.dms.dailyjoy.ui.settings.SettingsScreen
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainActivityContent()
        }
    }
}

@Composable
fun MainActivityContent() {
    val navController = rememberNavController()

    DailyJoyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
                },
                bottomBar = {
                    BottomNavBar(navController = navController)
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavHost(navController = navController, startDestination = HomeRoute) {
                        composable<HomeRoute> { HomeScreen() }
                        composable<HistoryRoute> { HistoryScreen() }
                        composable<SettingsRoute> { SettingsScreen() }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun MainActivityContentPreview() {
    DailyJoyTheme {
        MainActivityContent()
    }
}