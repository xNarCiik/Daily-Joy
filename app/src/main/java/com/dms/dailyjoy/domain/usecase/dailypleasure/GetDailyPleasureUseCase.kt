package com.dms.dailyjoy.domain.usecase.dailypleasure

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.DailyPleasureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Récupère le plaisir qui a été tiré pour aujourd'hui.
 * Si aucun plaisir n'a été tiré ou si la date sauvegardée est obsolète, retourne null.
 */
class GetDailyPleasureUseCase @Inject constructor(
    private val dailyPleasureRepository: DailyPleasureRepository
) {
    operator fun invoke(): Flow<Pleasure?> {
        return dailyPleasureRepository.getDailyPleasure()
    }
}
