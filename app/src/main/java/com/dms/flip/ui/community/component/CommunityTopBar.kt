package com.dms.flip.ui.community.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.dms.flip.R
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview

@Composable
fun CommunityTopBar(
    modifier: Modifier = Modifier,
    pendingRequestsCount: Int,
    onFriendsListClick: () -> Unit,
    onInvitationsClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onFriendsListClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = stringResource(R.string.friends_list),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = stringResource(R.string.community_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            BadgedBox(
                badge = {
                    if (pendingRequestsCount > 0) {
                        Badge(containerColor = MaterialTheme.colorScheme.error) {
                            Text(pendingRequestsCount.toString())
                        }
                    }
                }
            ) {
                IconButton(
                    onClick = onInvitationsClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = stringResource(R.string.community_invitations),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

private class CommunityTopBarPreviewParameterProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int> = sequenceOf(0, 3)
}

@LightDarkPreview
@Composable
private fun CommunityTopBarPreview(
    @PreviewParameter(CommunityTopBarPreviewParameterProvider::class)
    pendingRequests: Int
) {
    FlipTheme {
        CommunityTopBar(
            pendingRequestsCount = pendingRequests,
            onFriendsListClick = {},
            onInvitationsClick = {}
        )
    }
}
