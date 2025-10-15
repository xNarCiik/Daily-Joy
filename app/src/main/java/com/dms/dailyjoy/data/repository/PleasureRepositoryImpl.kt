package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.local.LocalPleasureDataSource
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository

class PleasureRepositoryImpl : PleasureRepository {
    override fun getPleasures(): List<Pleasure> = LocalPleasureDataSource.pleasure

    override fun getRandomPleasure(): Pleasure = getPleasures().random()
}