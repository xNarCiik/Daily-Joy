package com.dms.flip.domain.model

import com.dms.flip.data.model.PleasureCategory

/**
 * Entrée d'historique d'un plaisir tiré / réalisé.
 */
data class PleasureHistory(
    val id: String = "",
    val dateDrawn: Long = 0L,
    val completed: Boolean = false,
    val pleasureTitle: String? = null,
    val pleasureCategory: PleasureCategory? = null,
    val pleasureDescription: String? = null,
    val completedAt: Long? = null
) {
    fun toPleasureOrNull(): Pleasure? {
        val title = pleasureTitle ?: return null
        val category = pleasureCategory ?: return null
        return Pleasure(
            id = id,
            title = title,
            description = pleasureDescription.orEmpty(),
            category = category
        )
    }
}
