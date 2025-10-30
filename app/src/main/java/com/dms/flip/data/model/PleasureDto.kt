package com.dms.flip.data.model

import com.dms.flip.domain.model.Pleasure

data class PleasureDto(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val isEnabled: Boolean = true
) {
    fun toDomain(id: String): Pleasure {
        return Pleasure(
            id = id,
            title = title,
            description = description,
            category = PleasureCategory.valueOf(category),
            isEnabled = isEnabled
        )
    }
}

fun Pleasure.toDto(): PleasureDto {
    return PleasureDto(
        title = title,
        description = description,
        category = category.name,
        isEnabled = isEnabled
    )
}
