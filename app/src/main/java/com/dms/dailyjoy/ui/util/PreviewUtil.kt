package com.dms.dailyjoy.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.ui.social.Friend
import com.dms.dailyjoy.ui.social.FriendPleasure
import com.dms.dailyjoy.ui.social.PleasureStatus

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class LightDarkPreview

val previewDailyPleasure = Pleasure(
    id = 0,
    title = "Pleasure Title",
    description = "Pleasure Description",
    type = PleasureType.BIG,
    category = PleasureCategory.CREATIVE
)

val previewFriends = listOf(
    Friend(
        id = "1",
        username = "Dams",
        streak = 8,
        currentPleasure = FriendPleasure(
            title = "Roule un pet (ou 2...)",
            status = PleasureStatus.COMPLETED
        )
    ),
    Friend(
        id = "2",
        username = "Emma",
        streak = 326,
        currentPleasure = FriendPleasure(
            title = "Baiser Hugo",
            status = PleasureStatus.IN_PROGRESS
        )
    ),
    Friend(
        id = "3",
        username = "Alisson",
        streak = 2,
        currentPleasure = FriendPleasure(
            title = "Faire une bouffe XXL",
            status = PleasureStatus.IN_PROGRESS
        )
    ),
    Friend(
        id = "4",
        username = "Lilou la fripouille",
        streak = 33,
        currentPleasure = FriendPleasure(
            title = "Appeler mon petit frère et lui tapper la causette h24",
            status = PleasureStatus.COMPLETED
        )
    )
)