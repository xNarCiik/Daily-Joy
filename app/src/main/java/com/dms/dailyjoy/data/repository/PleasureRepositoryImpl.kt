package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.mapper.toDomain
import com.dms.dailyjoy.data.database.mapper.toEntity
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

    override fun getPleasuresCount(): Flow<Int> {
        return getAllPleasures().map { pleasures ->
            pleasures.count { it.isEnabled }
        }
    }

    override fun getPleasureCategories(): Flow<List<PleasureCategory>> {
        return flowOf(PleasureCategory.entries.filter { it == PleasureCategory.ALL })
    }

    override fun getRandomPleasure(category: PleasureCategory?): Flow<Pleasure> {
        return getAllPleasures().map { pleasures ->
            val filteredList =
                pleasures.filter { it.isEnabled && (category == PleasureCategory.ALL || category == null || it.category == category) }
            if (filteredList.isEmpty()) {
                throw IllegalStateException("No pleasures available for this category.")
            }
            filteredList.random()
        }
    }

    override suspend fun insert(pleasure: Pleasure) = pleasureDao.insert(pleasure.toEntity())

    override suspend fun update(pleasure: Pleasure) = pleasureDao.update(pleasure.toEntity())

    override suspend fun delete(pleasure: Pleasure) = pleasureDao.delete(pleasure.toEntity())
}
