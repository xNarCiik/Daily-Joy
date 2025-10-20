package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.mapper.toDomain
import com.dms.dailyjoy.data.database.mapper.toEntity
import com.dms.dailyjoy.data.local.LocalPleasureDataSource
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PleasureRepositoryImpl @Inject constructor(private val pleasureDao: PleasureDao) :
    PleasureRepository {

    override fun getAllPleasures(): Flow<List<Pleasure>> =
        pleasureDao.getAllPleasures().map { entities ->
            val dbPleasures = entities.map { it.toDomain() }
            val localPleasures = LocalPleasureDataSource.pleasure
            dbPleasures + localPleasures
        }

    override suspend fun getPleasureById(id: Int): Pleasure? =
        pleasureDao.getPleasureById(id)?.toDomain()
            ?: LocalPleasureDataSource.pleasure.find { it.id == id }

    override suspend fun insert(pleasure: Pleasure) = pleasureDao.insert(pleasure.toEntity())

    override suspend fun delete(pleasure: Pleasure) = pleasureDao.delete(pleasure.toEntity())
}
