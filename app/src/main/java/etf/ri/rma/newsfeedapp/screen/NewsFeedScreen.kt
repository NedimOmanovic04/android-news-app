package etf.ri.rma.newsfeedapp.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.viewmodel.NewsFeedViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen(navController: NavHostController) {
    // Uzmi ViewModel
    val viewModel: NewsFeedViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ViewModelStoreOwner
    )

    // Pretplati se na vijesti iz ViewModel-a
    val allNews by viewModel.allNewsFlow.collectAsState()

    // Primijeni filtere i izbriši duplikate po uuid
    val filtered = remember(allNews, viewModel.selectedCategory, viewModel.selectedDateRange, viewModel.unwantedWords) {
        applyFilters(allNews, viewModel).distinctBy { it.uuid }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NewsFeedApp") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor      = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor   = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                CategoryFilters(
                    selectedCategory    = viewModel.selectedCategory,
                    onCategorySelected  = { viewModel.onCategorySelected(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick  = { navController.navigate("filters") },
                    modifier = Modifier.testTag("filter_chip_more")
                ) {
                    Text("Više filtera ...")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NewsList(
                news       = filtered,
                onNewsClick = { newsItem ->
                    viewModel.loadDetailsData(newsItem) {
                        navController.navigate("details/${newsItem.uuid}")
                    }
                }
            )
        }
    }
}

private fun applyFilters(
    news:       List<NewsItem>,
    viewModel:  NewsFeedViewModel
): List<NewsItem> {
    return news.filter { item ->
        // 1) Kategorija
        (viewModel.selectedCategory == "general"
                || item.category.equals(viewModel.selectedCategory, ignoreCase = true))
                // 2) DateRange
                && (viewModel.selectedDateRange?.let { range ->
            val itemDate = LocalDate.parse(item.publishedDate)
            !itemDate.isBefore(range.start) && !itemDate.isAfter(range.end)
        } ?: true)
                // 3) Neželjene riječi
                && viewModel.unwantedWords.none { word ->
            item.title.contains(word, ignoreCase = true)
                    || item.snippet.contains(word, ignoreCase = true)
        }
    }
}
