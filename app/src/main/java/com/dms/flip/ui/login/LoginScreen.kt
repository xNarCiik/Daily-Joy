package com.dms.flip.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dms.flip.R
import com.dms.flip.ui.component.AppIcon
import com.dms.flip.ui.component.LoadingState
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val credentialManager = remember { CredentialManager.create(context) }

    val googleIdOption = GetGoogleIdOption.Builder() // TODO STRING
        .setServerClientId("251003841383-uiaqo6aq7himht7bt3d78aeaqga456h3.apps.googleusercontent.com")
        .setFilterByAuthorizedAccounts(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

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
                        colors = if (isSystemInDarkTheme()) {
                            listOf(
                                Color(0xFF1A1A2E),
                                Color(0xFF16213E),
                                Color(0xFF0F3460)
                            )
                        } else {
                            listOf(
                                Color(0xFFFFF9E6),
                                Color(0xFFFFF0F5),
                                Color(0xFFE8F4F8)
                            )
                        }
                    )
                )
                .padding(padding)
        ) {
            val isLoading = uiState is LoginUiState.Loading

            LoginContent(
                isLoading = isLoading,
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
                                e.localizedMessage
                                    ?: context.getString(R.string.login_google_failed)
                            )
                        }
                    }
                }
            )

            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LoadingState(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun LoginContent(
    isLoading: Boolean,
    onGoogleSignInClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme() // TODO EXPORT THEME

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
                        colors = if (isDarkTheme) {
                            listOf(
                                Color(0xFFFF6B9D).copy(alpha = 0.3f),
                                Color(0xFFFF6B9D).copy(alpha = 0.1f)
                            )
                        } else {
                            listOf(
                                Color(0xFFFFD700).copy(alpha = 0.3f),
                                Color(0xFFFFB6C1).copy(alpha = 0.2f)
                            )
                        }
                    ),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            AppIcon(modifier = Modifier.size(64.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) Color(0xFFECF0F1) else Color(0xFF2C3E50)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.login_start_day_with_joy),
            style = MaterialTheme.typography.titleMedium,
            color = if (isDarkTheme) Color(0xFFBDC3C7) else Color(0xFF7F8C8D),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.login_daily_happiness),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isDarkTheme) Color(0xFF95A5A6) else Color(0xFFBDC3C7),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1.5f))

        GoogleSignInButton(
            onClick = onGoogleSignInClick,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(id = R.string.login_terms_and_conditions),
            style = MaterialTheme.typography.bodySmall,
            color = if (isDarkTheme) Color(0xFF7F8C8D) else Color(0xFFBDC3C7),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF2C3E50),
            disabledContainerColor = Color.White.copy(alpha = 0.6f),
            disabledContentColor = Color(0xFF2C3E50).copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 2.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = stringResource(id = R.string.login_google_logo),
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(id = R.string.login_continue_with_google),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun LoginScreenPreview() {
    FlipTheme {
        Surface {
            LoginScreen()
        }
    }
}
