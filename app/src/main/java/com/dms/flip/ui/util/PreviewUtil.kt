package com.dms.flip.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.ui.history.WeeklyDay
import com.dms.flip.ui.social.Friend
import com.dms.flip.ui.social.FriendPleasure
import com.dms.flip.ui.social.PleasureStatus

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class LightDarkPreview

val previewDailyPleasure = Pleasure(
    id = "0",
    title = "Pleasure Title",
    description = "Pleasure Description",
    category = PleasureCategory.CREATIVE
)

val previewFriends = listOf(
    Friend(
        id = "1",
        username = "Dams",
        streak = 8,
        currentPleasure = FriendPleasure(
            title = "Roule un pet (ou 2...)",
            status = PleasureStatus.COMPLETED,
            category = PleasureCategory.ALL
        ),
        avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/456069928_491069416973778_8102649957987195103_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=106&_nc_oc=Q6cZ2QG4rVoBJ6sEYElGW-AZn5ifCGVV4Qg-RnrUnVQSXmgzZJ189fPNePshBzEbq9hzsUwZ3_S0Oj78n-tW1lxsk1El&_nc_ohc=69HpQ1dXh1oQ7kNvwGew0jG&_nc_gid=qQvGEAaks1sFiF5Pgln3TA&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_Afe5-ZY9gDxZDwHDRhtevkGtBWHwxZb0UOl1sjrroCMmqg&oe=6905BB85&_nc_sid=7a9f4b"
    ),
    Friend(
        id = "2",
        username = "Emma",
        streak = 326,
        currentPleasure = FriendPleasure(
            title = "Baiser Ugo",
            status = PleasureStatus.IN_PROGRESS,
            category = PleasureCategory.FOOD
        ),
        avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/401916050_1385369609057390_7192697213845317191_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=102&_nc_oc=Q6cZ2QG4mR4RIAnPWJkdfmmNTU9oeuRlKgEE3kTQU4lF92yTXp8jSKMijP1KZ55MRlykKk4nEuVru-gHA16dRm1c-sXO&_nc_ohc=RFejnkIOLcQQ7kNvwF9_ktT&_nc_gid=LYJo9EvRL8Air7a2-OiXpw&edm=APoiHPcBAAAA&ccb=7-5&oh=00_AfeVT-3g_LHDNgjFYrchKVMdASIzoWca0h9-ip0aAUPMnQ&oe=6905CF01&_nc_sid=22de04"
    ),
    Friend(
        id = "3",
        username = "Alisson",
        streak = 2,
        avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/525189438_18151745419389304_842152024012543561_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmMyIn0&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=104&_nc_oc=Q6cZ2QFRY4LwbkbPH1o2W_p0VzGJt1r0vqzYerN_8TsB4PXSNpx5USn-CoIxZ1gM7IioBtJ-XR43LminTMMTURd8ztCY&_nc_ohc=QIvDbT-G2_AQ7kNvwHp6Fza&_nc_gid=fd7JWnTKgdG63EvhrVlNCg&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_AfdOjvelQI3znS_i6a2z-T1CdP3VPhs26YuZQegoLjO3RA&oe=6905BBC5&_nc_sid=7a9f4b"
    ),
    Friend(
        id = "4",
        username = "Antho",
        streak = 2,
        currentPleasure = FriendPleasure(
            title = "GROSSSE TEUFFFF",
            status = PleasureStatus.IN_PROGRESS,
            category = PleasureCategory.ENTERTAINMENT
        ),
        avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/502718768_18065806517284093_1519982854029510988_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby4xMDgwLmV4cGVyaW1lbnRhbCJ9&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=101&_nc_oc=Q6cZ2QG4VLM_L5sBKFBia4cPbNgPJ43treZK8vAKwBM3Ut-cNBHfA9zMAbVUGCNlherSvAM74r7RdvQXRkyeHZ7_QdqX&_nc_ohc=pfY-A-GA6qsQ7kNvwHfacbe&_nc_gid=e7tUP4MQXedgTCgMy1xN1w&edm=ALGbJPMBAAAA&ccb=7-5&oh=00_Afd_jgFnDoADkxlT2qWkVcELEhbascTUVwnFKTtY0ODp2Q&oe=6905B1C1&_nc_sid=7d3ac5"
    ),
    Friend(
        id = "5",
        username = "Lilou la fripouille",
        streak = 33,
        currentPleasure = FriendPleasure(
            title = "Appeler mon petit frère et lui tapper la causette h24",
            status = PleasureStatus.COMPLETED,
            category = PleasureCategory.SOCIAL
        ),
        avatarUrl = "https://instagram.frns1-1.fna.fbcdn.net/v/t51.2885-19/543112697_18301432420247093_348652955831382183_n.jpg?efg=eyJ2ZW5jb2RlX3RhZyI6InByb2ZpbGVfcGljLmRqYW5nby41NDkuYzIifQ&_nc_ht=instagram.frns1-1.fna.fbcdn.net&_nc_cat=105&_nc_oc=Q6cZ2QEE6lZarhdFjXx3xHmMEZsR_baiOqSHKIXHktWDZ8P1janqlnFy_0NacBb3jnxCZEWaHe3rYc7oZSO1qLnpexd0&_nc_ohc=uWd8IyzNH58Q7kNvwFXQHjy&_nc_gid=-5FDa0QEFTWLM6xUhBpTvw&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_Afcq1SdfVr6AaoiNk0G24x-VIF-CTrQyWDDmy6XKY2ARVw&oe=6905CD5F&_nc_sid=7a9f4b"
    )
)

val previewWeeklyDays = listOf(
    WeeklyDay(
        dayName = "Lundi",
        historyEntry = PleasureHistory(
            id = "1",
            dateDrawn = System.currentTimeMillis() - 86400000 * 2,
            completed = true,
            pleasureTitle = "Savourer un café chaud",
            pleasureDescription = "Prendre le temps de déguster",
            pleasureCategory = PleasureCategory.FOOD
        ),
        dateMillis = 0
    ),
    WeeklyDay(
        dayName = "Mardi",
        historyEntry = PleasureHistory(
            id = "2",
            dateDrawn = System.currentTimeMillis() - 86400000,
            completed = true,
            pleasureTitle = "Lire quelques pages d'un livre",
            pleasureDescription = "Se plonger dans une histoire",
            pleasureCategory = PleasureCategory.LEARNING
        ),
        dateMillis = 0
    ),
    WeeklyDay(
        dayName = "Mercredi",
        historyEntry = PleasureHistory(
            id = "3",
            dateDrawn = System.currentTimeMillis(),
            completed = false,
            pleasureTitle = "Plaisir du jour",
            pleasureDescription = "",
            pleasureCategory = PleasureCategory.ALL
        ),
        dateMillis = 0
    ),
    WeeklyDay(dayName = "Jeudi", historyEntry = null, dateMillis = 0),
    WeeklyDay(dayName = "Vendredi", historyEntry = null, dateMillis = 0),
    WeeklyDay(dayName = "Samedi", historyEntry = null, dateMillis = 0),
    WeeklyDay(dayName = "Dimanche", historyEntry = null, dateMillis = 0)
)
