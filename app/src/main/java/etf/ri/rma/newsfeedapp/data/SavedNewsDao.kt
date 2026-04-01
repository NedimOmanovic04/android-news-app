package etf.ri.rma.newsfeedapp.data

import androidx.room.*
import etf.ri.rma.newsfeedapp.data.local.entity.*
import etf.ri.rma.newsfeedapp.data.local.relation.NewsWithTags
import etf.ri.rma.newsfeedapp.model.NewsItem

@Dao
interface SavedNewsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(entity: NewsEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(ref: NewsTagCrossRef)

    @Query("SELECT * FROM News")
    suspend fun loadAllNewsEntities(): List<NewsEntity>

    @Query("SELECT * FROM News WHERE category = :category")
    suspend fun loadNewsByCategory(category: String): List<NewsEntity>

    @Query("SELECT * FROM Tags WHERE value = :value LIMIT 1")
    suspend fun findTagByValue(value: String): TagEntity?

    @Query("SELECT id FROM News WHERE uuid = :uuid LIMIT 1")
    suspend fun findIdByUuid(uuid: String): Int?

    @Query("SELECT id FROM News WHERE title = :title LIMIT 1")
    suspend fun findIdByTitle(title: String): Int?

    @Transaction
    @Query("SELECT * FROM News WHERE id = :id")
    suspend fun loadNewsWithTags(id: Int): NewsWithTags

    @Transaction
    @Query(
        """
        SELECT * FROM News
        INNER JOIN NewsTags ON News.id  = NewsTags.newsId
        INNER JOIN Tags     ON Tags.id  = NewsTags.tagsId
        WHERE Tags.value IN (:tags)
        ORDER BY publishedDate DESC
    """
    )
    suspend fun loadSimilarNews(tags: List<String>): List<NewsWithTags>

    @Query("SELECT * FROM Tags")
    suspend fun getAllTags(): List<TagEntity>



    @Transaction
    suspend fun saveNews(news: NewsItem): Boolean {
        if (findIdByUuid(news.uuid) != null) return false
        if (findIdByTitle(news.title) != null) return false
        return insertNews(news.toEntity()) != -1L
    }

    @Transaction
    suspend fun allNews(): List<NewsItem> =
        loadAllNewsEntities().map { it.toNewsItem(loadNewsWithTags(it.id).tags) }

    @Transaction
    suspend fun getNewsWithCategory(category: String): List<NewsItem> =
        loadNewsByCategory(category).map { it.toNewsItem(loadNewsWithTags(it.id).tags) }

    @Transaction
    suspend fun addTags(tags: List<String>, newsId: Int): Int {
        var added = 0
        for (value in tags) {
            val tagId = findTagByValue(value)?.id ?: run {
                val newId = insertTag(TagEntity(value = value)).toInt()
                if (newId != -1) added++
                newId
            }
            insertCrossRef(NewsTagCrossRef(newsId = newsId, tagsId = tagId))
        }
        return added
    }

    @Transaction
    suspend fun getTags(newsId: Int): List<String> =
        loadNewsWithTags(newsId).tags.map { it.value }

    @Transaction
    suspend fun getSimilarNews(tags: List<String>): List<NewsItem> =
        loadSimilarNews(tags.take(2)).map { rel -> rel.news.toNewsItem(rel.tags) }



    private fun NewsItem.toEntity() = NewsEntity(
        uuid = uuid,
        title = title,
        snippet = snippet,
        source = source,
        publishedDate = publishedDate,
        imageUrl = imageUrl,
        category = category,
        isFeatured = isFeatured
    )

    private fun NewsEntity.toNewsItem(tags: List<TagEntity>) = NewsItem(
        uuid = uuid,
        title = title,
        snippet = snippet,
        source = source,
        publishedDate = publishedDate,
        imageUrl = imageUrl,
        category = category,
        isFeatured = isFeatured,
        imageTags = tags
    )
}
