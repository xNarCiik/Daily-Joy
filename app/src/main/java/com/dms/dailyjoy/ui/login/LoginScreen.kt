package com.dms.dailyjoy.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val credentialManager = remember { CredentialManager.create(context) }

    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId("251003841383-uiaqo6aq7himht7bt3d78aeaqga456h3.apps.googleusercontent.com")
        .setFilterByAuthorizedAccounts(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            if (event is LoginNavigationEvent.NavigateToHome) {
                onNavigateToHome()
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Error) {
            snackbarHostState.showSnackbar(
                message = (uiState as LoginUiState.Error).message
            )
            viewModel.clearError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFF9E6),
                            Color(0xFFFFF0F5),
                            Color(0xFFE8F4F8)
                        )
                    )
                )
                .padding(padding)
        ) {
            LoginContent(
                onGoogleSignInClick = {
                    coroutineScope.launch {
                        try {
                            val result = credentialManager.getCredential(
                                request = request,
                                context = context
                            )

                            val credential = result.credential

                            if (credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)

                                viewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar(
                                e.localizedMessage ?: "Connexion Google échouée"
                            )
                        }
                    }
                }
            )

            AnimatedVisibility(
                visible = uiState is LoginUiState.Loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(56.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginContent(onGoogleSignInClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "iconScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.3f),
                            Color(0xFFFFB6C1).copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "DailyJoy Logo",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFFFF6B9D)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Commencez votre journée avec joie",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF7F8C8D),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Votre dose de bonheur quotidienne",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFBDC3C7),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1.5f))

        GoogleSignInButton(onClick = onGoogleSignInClick)

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "En continuant, vous acceptez nos conditions d'utilisation et notre politique de confidentialité",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFBDC3C7),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF2C3E50)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Continuer avec Google",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun LoginScreenPreview() {
    DailyJoyTheme {
        Surface {
            LoginScreen(onNavigateToHome = {})
        }
    }
}
