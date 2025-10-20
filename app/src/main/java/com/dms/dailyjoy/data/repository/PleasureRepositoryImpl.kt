package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.mapper.toDomain
import com.dms.dailyjoy.data.database.mapper.toEntity
import com.dms.dailyjoy.data.local.LocalPleasureDataSource
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
import javax.inject.Inject

class PleasureRepositoryImpl @Inject constructor(
    private val pleasureDao: PleasureDao,
    private val localDataSource: LocalPleasureDataSource
) : PleasureRepository {

    override suspend fun getAllPleasures(): List<Pleasure> {
        val pleasures = pleasureDao.getAllPleasures().map { pleasure ->
            pleasure.toDomain()
        }
        val localPleasures = localDataSource.getPleasures()
        return pleasures + localPleasures
    }

    override suspend fun getPleasureById(id: Int): Pleasure? {
        val pleasureFromDb = pleasureDao.getPleasureById(id)?.toDomain()
        if (pleasureFromDb != null) {
            return pleasureFromDb
        }
        return localDataSource.getPleasures().find { it.id == id }
    }

    override suspend fun insert(pleasure: Pleasure) = pleasureDao.insert(pleasure.toEntity())

    override suspend fun delete(pleasure: Pleasure) = pleasureDao.delete(pleasure.toEntity())
}
