package com.dms.flip.domain.usecase.weekly

import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.max

/**
 * Flux "large" d’historique sur ~180 jours.
 * Le ViewModel filtre ensuite sur la semaine affichée (weekOffset).
 * Avantage : pas besoin de redéclencher une requête Firestore pour chaque swipe de semaine,
 * tout reste réactif en local.
 */
class GetWeeklyHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<List<PleasureHistory>> {
        val cal = Calendar.getInstance().apply {
            // borne haute = maintenant (exclusif arrondi à demain 00:00 pour ne pas louper la journée)
            set(Calendar.MILLISECOND, 0)
        }
        val end = cal.apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1) // demain 00:00 (exclusif)
        }.timeInMillis

        // borne basse = ~180 jours en arrière (6 mois)
        val start = cal.apply {
            timeInMillis = end
            add(Calendar.DAY_OF_YEAR, -180)
        }.timeInMillis

        // Par sécurité, s’assure que start < end
        val startSafe = max(0L, start)

        return historyRepository.getHistoryForDateRange(startSafe, end)
    }
}
