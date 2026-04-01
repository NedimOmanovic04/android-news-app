package etf.ri.rma.newsfeedapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import etf.ri.rma.newsfeedapp.data.local.entity.NewsEntity
import etf.ri.rma.newsfeedapp.data.local.entity.NewsTagCrossRef
import etf.ri.rma.newsfeedapp.data.local.entity.TagEntity

data class NewsWithTags(
    @Embedded val news: NewsEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsTagCrossRef::class,
            parentColumn = "newsId",
            entityColumn = "tagsId"
        )
    )
    val tags: List<TagEntity>
)
