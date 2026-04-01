package etf.ri.rma.newsfeedapp.data.local.dao

import androidx.room.*
import etf.ri.rma.newsfeedapp.data.local.entity.NewsEntity
import etf.ri.rma.newsfeedapp.data.local.relation.NewsWithTags

@Dao
interface NewsDao {
    @Query("SELECT * FROM News")
    suspend fun getAll(): List<NewsEntity>

    @Transaction
    @Query("SELECT * FROM News WHERE id = :id")
    suspend fun getWithTags(id: Int): NewsWithTags
}
