package etf.ri.rma.newsfeedapp.data.local.dao

import androidx.room.*
import etf.ri.rma.newsfeedapp.data.local.entity.TagEntity
import etf.ri.rma.newsfeedapp.data.local.entity.NewsTagCrossRef

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: TagEntity): Long

    @Query("SELECT * FROM Tags WHERE value = :v LIMIT 1")
    suspend fun findByValue(v: String): TagEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(ref: NewsTagCrossRef)
}
