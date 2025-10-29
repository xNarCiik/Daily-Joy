package com.dms.flip.ui.login

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dms.flip.R
import com.dms.flip.ui.component.LoadingState
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.theme.flipGradients
import com.dms.flip.ui.util.LightDarkPreview
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToTerms: () -> Unit,
    navigateToPolicy: () -> Unit
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

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Error) {
            snackbarHostState.showSnackbar(
                message = (uiState as LoginUiState.Error).message
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = flipGradients().setupBackground)
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
                },
                navigateToTerms = navigateToTerms,
                navigateToPolicy = navigateToPolicy
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
    onGoogleSignInClick: () -> Unit,
    navigateToTerms: () -> Unit,
    navigateToPolicy: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Section centrale : Logo + Textes
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Logo unique de l'app
            FlipLogo()

            Spacer(modifier = Modifier.height(48.dp))

            // Titre principal
            Text(
                text = stringResource(R.string.login_start_day_with_joy),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    lineHeight = 40.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sous-titre
            Text(
                text = stringResource(id = R.string.login_daily_happiness),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }

        // Section bottom : Bouton + Terms
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            GoogleSignInButton(
                onClick = onGoogleSignInClick,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            TermsAndConditionsText(
                navigateToTerms = navigateToTerms,
                navigateToPolicy = navigateToPolicy
            )
        }
    }
}

@Composable
private fun FlipLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(140.dp)
            .background(
                brush = flipGradients().logo,
                shape = MaterialTheme.shapes.large
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "F",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-2).sp
            ),
            color = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
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
                tint = androidx.compose.ui.graphics.Color.Unspecified
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(id = R.string.login_continue_with_google),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
private fun TermsAndConditionsText(
    modifier: Modifier = Modifier,
    navigateToTerms: () -> Unit,
    navigateToPolicy: () -> Unit
) {
    val baseColor = MaterialTheme.colorScheme.onSurfaceVariant
    val linkColor = MaterialTheme.colorScheme.primary

    val termsOfUse = stringResource(id = R.string.terms_of_use)
    val privacyPolicy = stringResource(id = R.string.privacy_policy)

    val annotatedString = buildAnnotatedString {
        val fullText = stringResource(id = R.string.login_terms_and_conditions, termsOfUse, privacyPolicy)
        val termsIndex = fullText.indexOf(termsOfUse)
        val policyIndex = fullText.indexOf(privacyPolicy)

        append(fullText)

        if (termsIndex != -1) {
            addStyle(
                style = SpanStyle(
                    color = linkColor,
                    fontWeight = FontWeight.Medium
                ),
                start = termsIndex,
                end = termsIndex + termsOfUse.length
            )
            addStringAnnotation(
                tag = "URL",
                annotation = "TERMS",
                start = termsIndex,
                end = termsIndex + termsOfUse.length
            )
        }

        if (policyIndex != -1) {
            addStyle(
                style = SpanStyle(
                    color = linkColor,
                    fontWeight = FontWeight.Medium
                ),
                start = policyIndex,
                end = policyIndex + privacyPolicy.length
            )
            addStringAnnotation(
                tag = "URL",
                annotation = "POLICY",
                start = policyIndex,
                end = policyIndex + privacyPolicy.length
            )
        }
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            color = baseColor
        ),
        modifier = modifier.padding(horizontal = 16.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let {
                    when (it.item) {
                        "TERMS" -> navigateToTerms()
                        "POLICY" -> navigateToPolicy()
                    }
                }
        }
    )
}

@LightDarkPreview
@Composable
private fun LoginScreenPreview() {
    FlipTheme {
        Surface {
            LoginScreen(
                navigateToTerms = {},
                navigateToPolicy = {}
            )
        }
    }
}
