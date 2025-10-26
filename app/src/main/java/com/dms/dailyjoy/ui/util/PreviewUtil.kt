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
        ),
        avatarUrl = "https://imgs.search.brave.com/AcI86uv_5R_MljLp-jhJLLKldiWrXlTbkKN5i1lrFMA/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4w/Lmljb25maW5kZXIu/Y29tL2RhdGEvaWNv/bnMvcGVvcGxlLTEz/Ny81MTMvZ2FtZXIt/NTEyLnBuZw"
    ),
    Friend(
        id = "2",
        username = "Emma",
        streak = 326,
        currentPleasure = FriendPleasure(
            title = "Baiser Hugo",
            status = PleasureStatus.IN_PROGRESS
        ),
        avatarUrl = "https://media.istockphoto.com/id/1346787021/fr/photo/chat-pli%C3%A9-surpris-dans-un-chapeau-danniversaire-de-f%C3%AAte-espace-de-copie.jpg?s=612x612&w=0&k=20&c=N0bA8ZddvSKqXjSwcxFufGaPh_OB_0tpobF9Z56HFdg="
    ),
    Friend(
        id = "3",
        username = "Alisson",
        streak = 2,
        currentPleasure = FriendPleasure(
            title = "Faire une bouffe XXL",
            status = PleasureStatus.IN_PROGRESS
        ),
        avatarUrl = "https://media.vanityfair.fr/photos/60d37b2d768549db8994a8ee/16:9/w_2240,c_limit/vf_avatar_cover_2953.jpeg"
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