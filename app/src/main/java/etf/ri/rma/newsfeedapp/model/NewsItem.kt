package etf.ri.rma.newsfeedapp.model

import etf.ri.rma.newsfeedapp.data.local.entity.TagEntity

data class NewsItem(
    val uuid: String,
    val title: String,
    val snippet: String,
    val source: String,
    val publishedDate: String,
    val imageUrl: String,
    val category: String,
    var isFeatured: Boolean,
    var imageTags: List<TagEntity> = emptyList()
)
