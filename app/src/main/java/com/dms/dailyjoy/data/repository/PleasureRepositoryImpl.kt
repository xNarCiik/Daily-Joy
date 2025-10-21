package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.mapper.toDomain
import com.dms.dailyjoy.data.database.mapper.toEntity
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PleasureRepositoryImpl @Inject constructor(
    private val pleasureDao: PleasureDao
) : PleasureRepository {

    override fun getAllPleasures(): Flow<List<Pleasure>> {
        return pleasureDao.getAllPleasures().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getPleasureById(id: Int): Pleasure? =
        pleasureDao.getPleasureById(id)?.toDomain()

    override suspend fun insert(pleasure: Pleasure) = pleasureDao.insert(pleasure.toEntity())

    override suspend fun update(pleasure: Pleasure) = pleasureDao.update(pleasure.toEntity())

    override suspend fun delete(pleasure: Pleasure) = pleasureDao.delete(pleasure.toEntity())
}
