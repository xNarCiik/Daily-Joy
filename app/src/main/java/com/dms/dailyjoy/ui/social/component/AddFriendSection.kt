package com.dms.dailyjoy.ui.social.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.theme.dailyJoyGradients
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun AddFriendSection(
    modifier: Modifier = Modifier,
    username: String,
    isLoading: Boolean,
    error: String?,
    onUsernameChange: (String) -> Unit,
    onAddFriend: () -> Unit
) {
    val gradients = dailyJoyGradients()

    val buttonScale by animateFloatAsState(
        targetValue = if (username.isNotBlank() && !isLoading) 1f else 0.9f,
        label = "buttonScale"
    )

    Column(modifier = modifier) {
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    stringResource(R.string.social_add_friend_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            placeholder = {
                Text(
                    stringResource(R.string.social_username_placeholder),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            isError = error != null,
            enabled = !isLoading,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
            ),
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (error != null) {
                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                            } else {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = if (error != null) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = username.isNotBlank()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(38.dp)
                            .scale(buttonScale)
                            .clip(CircleShape)
                            .background(gradients.accent)
                            .clickable(enabled = !isLoading && username.isNotBlank()) {
                                onAddFriend()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.social_add_friend_button),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun AddFriendSectionPreview() {
    DailyJoyTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AddFriendSection(
                    username = "",
                    isLoading = false,
                    error = null,
                    onUsernameChange = {},
                    onAddFriend = {}
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun AddFriendSectionFilledPreview() {
    DailyJoyTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AddFriendSection(
                    username = "john_doe",
                    isLoading = false,
                    error = null,
                    onUsernameChange = {},
                    onAddFriend = {}
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun AddFriendSectionLoadingPreview() {
    DailyJoyTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AddFriendSection(
                    username = "john_doe",
                    isLoading = true,
                    error = null,
                    onUsernameChange = {},
                    onAddFriend = {}
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun AddFriendSectionErrorPreview() {
    DailyJoyTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AddFriendSection(
                    username = "invalid_user",
                    isLoading = false,
                    error = "Cet utilisateur n'existe pas",
                    onUsernameChange = {},
                    onAddFriend = {}
                )
            }
        }
    }
}
