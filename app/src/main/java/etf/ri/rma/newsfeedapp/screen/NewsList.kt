package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsList(
    news: List<NewsItem>,
    onNewsClick: (NewsItem) -> Unit
) {
    // izbriši duplikate po uuid
    val uniqueNews = remember(news) {
        news.distinctBy { it.uuid }
    }

    if (uniqueNews.isEmpty()) {
        MessageCard("Nema pronađenih vijesti u odabranoj kategoriji.")
    } else {
        LazyColumn(modifier = Modifier.testTag("news_list")) {
            items(
                items = uniqueNews,
                key   = { it.uuid }
            ) { item ->
                if (item.isFeatured) {
                    FeaturedNewsCard(
                        newsItem = item,
                        onClick  = { onNewsClick(item) }
                    )
                } else {
                    StandardNewsCard(
                        newsItem = item,
                        onClick  = { onNewsClick(item) }
                    )
                }
            }
        }
    }
}
