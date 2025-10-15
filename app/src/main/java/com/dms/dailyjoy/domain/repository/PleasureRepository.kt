package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.model.Pleasure

interface PleasureRepository {
    fun getPleasures(): List<Pleasure>
    fun getRandomPleasure(): Pleasure
}