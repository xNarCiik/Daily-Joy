package com.dms.dailyjoy.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureState

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class LightDarkPreview

val previewDailyPleasure = Pleasure(
    id = 0,
    title = "Pleasure Title",
    description = "Pleasure Description",
    type = PleasureType.BIG,
    category = PleasureCategory.CREATIVE,
    isFlipped = true
)

val previewDailyPleasureState = DailyPleasureState(
    isLoading = false,
    dailyMessage = "Et si aujourd'hui, on prenait le temps de...",
    dailyPleasure = previewDailyPleasure
)