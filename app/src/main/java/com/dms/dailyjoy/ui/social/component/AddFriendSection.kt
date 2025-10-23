package com.dms.dailyjoy.ui.social.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
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
    Column(modifier = modifier) {
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    stringResource(R.string.social_add_friend_label),
                    style = MaterialTheme.typography.bodySmall
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
            shape = RoundedCornerShape(14.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    tint = if (error != null) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = username.isNotBlank()
                ) {
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(enabled = !isLoading) { onAddFriend() },
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
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
            }
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun AddFriendSectionPreview() {
    DailyJoyTheme {
        Surface {
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

@LightDarkPreview
@Composable
private fun AddFriendSectionLoadingPreview() {
    DailyJoyTheme {
        Surface {
            AddFriendSection(
                username = "test",
                isLoading = true,
                error = null,
                onUsernameChange = {},
                onAddFriend = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun AddFriendSectionErrorPreview() {
    DailyJoyTheme {
        Surface {
            AddFriendSection(
                username = "test",
                isLoading = false,
                error = "Cet utilisateur n'existe pas",
                onUsernameChange = {},
                onAddFriend = {}
            )
        }
    }
}
