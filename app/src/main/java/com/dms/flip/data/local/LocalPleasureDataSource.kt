package com.dms.flip.data.local

import android.content.res.Resources
import com.dms.flip.R
import com.dms.flip.data.model.Pleasure
import com.dms.flip.data.model.PleasureCategory
import javax.inject.Inject

class LocalPleasureDataSource @Inject constructor(
    private val resources: Resources
) {
    fun getPleasures(): List<Pleasure> = listOf(
        Pleasure(
            id = 1,
            title = resources.getString(R.string.pleasure_relax_1_title),
            description = resources.getString(R.string.pleasure_relax_1_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 2,
            title = resources.getString(R.string.pleasure_culture_2_title),
            description = resources.getString(R.string.pleasure_culture_2_description),
            category = PleasureCategory.CULTURE,
            isEnabled = false
        ),
        Pleasure(
            id = 3,
            title = resources.getString(R.string.pleasure_relax_3_title),
            description = resources.getString(R.string.pleasure_relax_3_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 4,
            title = resources.getString(R.string.pleasure_creative_4_title),
            description = resources.getString(R.string.pleasure_creative_4_description),
            category = PleasureCategory.CREATIVE,
            isEnabled = false
        ),
        Pleasure(
            id = 5,
            title = resources.getString(R.string.pleasure_social_5_title),
            description = resources.getString(R.string.pleasure_social_5_description),
            category = PleasureCategory.SOCIAL,
            isEnabled = false
        ),
        Pleasure(
            id = 6,
            title = resources.getString(R.string.pleasure_food_6_title),
            description = resources.getString(R.string.pleasure_food_6_description),
            category = PleasureCategory.FOOD,
            isEnabled = false
        ),
        Pleasure(
            id = 7,
            title = resources.getString(R.string.pleasure_relax_7_title),
            description = resources.getString(R.string.pleasure_relax_7_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 8,
            title = resources.getString(R.string.pleasure_game_8_title),
            description = resources.getString(R.string.pleasure_game_8_description),
            category = PleasureCategory.ENTERTAINMENT,
            isEnabled = false
        ),
        Pleasure(
            id = 9,
            title = resources.getString(R.string.pleasure_nature_9_title),
            description = resources.getString(R.string.pleasure_nature_9_description),
            category = PleasureCategory.OUTDOOR,
            isEnabled = false
        ),
        Pleasure(
            id = 10,
            title = resources.getString(R.string.pleasure_creative_10_title),
            description = resources.getString(R.string.pleasure_creative_10_description),
            category = PleasureCategory.CREATIVE,
            isEnabled = false
        ),
        Pleasure(
            id = 11,
            title = resources.getString(R.string.pleasure_food_11_title),
            description = resources.getString(R.string.pleasure_food_11_description),
            category = PleasureCategory.FOOD,
            isEnabled = false
        ),
        Pleasure(
            id = 12,
            title = resources.getString(R.string.pleasure_relax_12_title),
            description = resources.getString(R.string.pleasure_relax_12_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 13,
            title = resources.getString(R.string.pleasure_nature_13_title),
            description = resources.getString(R.string.pleasure_nature_13_description),
            category = PleasureCategory.OUTDOOR,
            isEnabled = false
        ),
        Pleasure(
            id = 14,
            title = resources.getString(R.string.pleasure_shopping_14_title),
            description = resources.getString(R.string.pleasure_shopping_14_description),
            category = PleasureCategory.SHOPPING,
            isEnabled = false
        ),
        Pleasure(
            id = 15,
            title = resources.getString(R.string.pleasure_culture_15_title),
            description = resources.getString(R.string.pleasure_culture_15_description),
            category = PleasureCategory.CULTURE,
            isEnabled = false
        ),
        Pleasure(
            id = 16,
            title = resources.getString(R.string.pleasure_social_16_title),
            description = resources.getString(R.string.pleasure_social_16_description),
            category = PleasureCategory.SOCIAL,
            isEnabled = false
        ),
        Pleasure(
            id = 17,
            title = resources.getString(R.string.pleasure_relax_17_title),
            description = resources.getString(R.string.pleasure_relax_17_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 18,
            title = resources.getString(R.string.pleasure_culture_18_title),
            description = resources.getString(R.string.pleasure_culture_18_description),
            category = PleasureCategory.CULTURE,
            isEnabled = false
        ),
        Pleasure(
            id = 19,
            title = resources.getString(R.string.pleasure_creative_19_title),
            description = resources.getString(R.string.pleasure_creative_19_description),
            category = PleasureCategory.CREATIVE,
            isEnabled = false
        ),
        Pleasure(
            id = 20,
            title = resources.getString(R.string.pleasure_food_20_title),
            description = resources.getString(R.string.pleasure_food_20_description),
            category = PleasureCategory.FOOD,
            isEnabled = false
        ),
        Pleasure(
            id = 21,
            title = resources.getString(R.string.pleasure_relax_21_title),
            description = resources.getString(R.string.pleasure_relax_21_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 22,
            title = resources.getString(R.string.pleasure_creative_22_title),
            description = resources.getString(R.string.pleasure_creative_22_description),
            category = PleasureCategory.CREATIVE,
            isEnabled = false
        ),
        Pleasure(
            id = 23,
            title = resources.getString(R.string.pleasure_nature_23_title),
            description = resources.getString(R.string.pleasure_nature_23_description),
            category = PleasureCategory.OUTDOOR,
            isEnabled = false
        ),
        Pleasure(
            id = 24,
            title = resources.getString(R.string.pleasure_social_24_title),
            description = resources.getString(R.string.pleasure_social_24_description),
            category = PleasureCategory.SOCIAL,
            isEnabled = false
        ),
        Pleasure(
            id = 25,
            title = resources.getString(R.string.pleasure_food_25_title),
            description = resources.getString(R.string.pleasure_food_25_description),
            category = PleasureCategory.FOOD,
            isEnabled = false
        ),
        Pleasure(
            id = 26,
            title = resources.getString(R.string.pleasure_sport_26_title),
            description = resources.getString(R.string.pleasure_sport_26_description),
            category = PleasureCategory.SPORT,
            isEnabled = false
        ),
        Pleasure(
            id = 27,
            title = resources.getString(R.string.pleasure_culture_27_title),
            description = resources.getString(R.string.pleasure_culture_27_description),
            category = PleasureCategory.CULTURE,
            isEnabled = false
        ),
        Pleasure(
            id = 28,
            title = resources.getString(R.string.pleasure_creative_28_title),
            description = resources.getString(R.string.pleasure_creative_28_description),
            category = PleasureCategory.CREATIVE,
            isEnabled = false
        ),
        Pleasure(
            id = 29,
            title = resources.getString(R.string.pleasure_nature_29_title),
            description = resources.getString(R.string.pleasure_nature_29_description),
            category = PleasureCategory.OUTDOOR,
            isEnabled = false
        ),
        Pleasure(
            id = 30,
            title = resources.getString(R.string.pleasure_relax_30_title),
            description = resources.getString(R.string.pleasure_relax_30_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 31,
            title = resources.getString(R.string.pleasure_culture_31_title),
            description = resources.getString(R.string.pleasure_culture_31_description),
            category = PleasureCategory.CULTURE,
            isEnabled = false
        ),
        Pleasure(
            id = 32,
            title = resources.getString(R.string.pleasure_nature_32_title),
            description = resources.getString(R.string.pleasure_nature_32_description),
            category = PleasureCategory.OUTDOOR,
            isEnabled = false
        ),
        Pleasure(
            id = 33,
            title = resources.getString(R.string.pleasure_social_33_title),
            description = resources.getString(R.string.pleasure_social_33_description),
            category = PleasureCategory.SOCIAL,
            isEnabled = false
        ),
        Pleasure(
            id = 34,
            title = resources.getString(R.string.pleasure_creative_34_title),
            description = resources.getString(R.string.pleasure_creative_34_description),
            category = PleasureCategory.CREATIVE,
            isEnabled = false
        ),
        Pleasure(
            id = 35,
            title = resources.getString(R.string.pleasure_relax_35_title),
            description = resources.getString(R.string.pleasure_relax_35_description),
            category = PleasureCategory.WELLNESS,
            isEnabled = false
        ),
        Pleasure(
            id = 36,
            title = resources.getString(R.string.pleasure_sport_36_title),
            description = resources.getString(R.string.pleasure_sport_36_description),
            category = PleasureCategory.SPORT,
            isEnabled = false
        ),
        Pleasure(
            id = 37,
            title = resources.getString(R.string.pleasure_food_37_title),
            description = resources.getString(R.string.pleasure_food_37_description),
            category = PleasureCategory.FOOD,
            isEnabled = false
        ),
        Pleasure(
            id = 38,
            title = resources.getString(R.string.pleasure_social_38_title),
            description = resources.getString(R.string.pleasure_social_38_description),
            category = PleasureCategory.SOCIAL,
            isEnabled = false
        ),
        Pleasure(
            id = 39,
            title = resources.getString(R.string.pleasure_shopping_39_title),
            description = resources.getString(R.string.pleasure_shopping_39_description),
            category = PleasureCategory.SHOPPING,
            isEnabled = false
        ),
        Pleasure(
            id = 40,
            title = resources.getString(R.string.pleasure_culture_40_title),
            description = resources.getString(R.string.pleasure_culture_40_description),
            category = PleasureCategory.CULTURE,
            isEnabled = false
        )
    )
}
