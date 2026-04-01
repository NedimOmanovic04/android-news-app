package etf.ri.rma.newsfeedapp.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.viewmodel.NewsFeedViewModel
@Composable
fun NewsDetailsScreen(
    navController: NavHostController,
    newsId: String
) {
    val viewModel: NewsFeedViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ViewModelStoreOwner
    )

    // Dio gdje dohvaćamo konkretan newsItem iz onoga što je trenutno prikazano:
    val allNewsDisplayed by viewModel.allNewsFlow.collectAsState()
    val newsItem: NewsItem? = allNewsDisplayed.find { it.uuid == newsId }

    if (newsItem == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Vijest nije pronađena.", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Nazad")
            }
        }
        return
    }

    // Za povezane vijesti: koristimo SVE članke iz DAO-a (lokalni + web)
    val allStories = viewModel.getAllStories()
    val relatedNews = allStories
        .filter { it.category.equals(newsItem.category, ignoreCase = true) && it.uuid != newsItem.uuid }
        .sortedBy { it.publishedDate }
        .take(2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = newsItem.title,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .testTag("details_title"),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = newsItem.snippet,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .testTag("details_snippet")
        )
        Text(
            text = newsItem.source,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .testTag("details_source")
        )
        Text(
            text = newsItem.publishedDate,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .testTag("details_date")
        )

        Text(
            "Povezane vijesti iz iste kategorije:",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        relatedNews.forEachIndexed { index, related ->
            Text(
                text = related.title,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .testTag("related_news_title_${index + 1}")
                    .clickable {
                        navController.popBackStack()
                        navController.navigate("details/${related.uuid}")
                    }
            )
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Zatvori detalje")
        }
    }
}

