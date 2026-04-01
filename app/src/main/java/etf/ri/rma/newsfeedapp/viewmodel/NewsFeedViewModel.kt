package etf.ri.rma.newsfeedapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import etf.ri.rma.newsfeedapp.data.NewsDatabase
import etf.ri.rma.newsfeedapp.data.network.ImaggaDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.data.local.entity.TagEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

private const val TAG = "NewsFeedVM"

data class DateRange(val start: LocalDate, val end: LocalDate)

class NewsFeedViewModel(app: Application) : AndroidViewModel(app) {
    private val db = NewsDatabase.getInstance(app)
    private val newsDAO = NewsDAO
    private val imaggaDAO = ImaggaDAO

    var selectedCategory: String by mutableStateOf("general"); private set
    var selectedDateRange: DateRange? by mutableStateOf(null); private set
    var unwantedWords: List<String> by mutableStateOf(emptyList()); private set

    private val _allNewsFlow = MutableStateFlow<List<NewsItem>>(emptyList())
    val allNewsFlow: StateFlow<List<NewsItem>> = _allNewsFlow

    private val similarCache = mutableMapOf<String, List<NewsItem>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val news = db.savedNewsDAO().allNews()
            _allNewsFlow.value = news.distinctBy { it.uuid }
        }
    }

    fun updateFilters(category: String, dateRange: DateRange?, words: List<String>) {
        selectedCategory = category
        selectedDateRange = dateRange
        unwantedWords = words
    }

    fun getAllStories(): List<NewsItem> {
        return newsDAO.getAllStories()
    }

    fun onCategorySelected(newCategory: String) {
        selectedCategory = newCategory
        viewModelScope.launch(Dispatchers.IO) {
            val rawList = try {
                if (newCategory == "general") {
                    val fetched = newsDAO.getAllStories()
                    fetched.forEach { db.savedNewsDAO().saveNews(it) }
                    fetched
                } else {
                    val fetched = try {
                        newsDAO.getTopStoriesByCategory(newCategory).also {
                            it.forEach { db.savedNewsDAO().saveNews(it) }
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Cannot fetch $newCategory from network", e)
                        emptyList()
                    }
                    if (fetched.isNotEmpty()) {
                        db.savedNewsDAO().getNewsWithCategory(newCategory) + fetched
                    } else {
                        db.savedNewsDAO().getNewsWithCategory(newCategory)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in onCategorySelected", e)
                db.savedNewsDAO().getNewsWithCategory(newCategory)
            }

            val combined = rawList
                .distinctBy { it.uuid }
                .toMutableList()
                .apply {
                    shuffle()
                    forEach { it.isFeatured = false }
                    take(3).forEach { it.isFeatured = true }
                }

            _allNewsFlow.value = combined
        }
    }

    fun loadDetailsData(newsItem: NewsItem, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (newsItem.imageTags.isEmpty()) {
                    try {
                        val tagValues = imaggaDAO.getTags(newsItem.imageUrl)
                        val tagEntities = tagValues.map { TagEntity(value = it) }
                        newsItem.imageTags = tagEntities

                        val id = db.newsDao().getAll().firstOrNull { it.uuid == newsItem.uuid }?.id
                        if (id != null) {
                            db.savedNewsDAO().addTags(tagValues, id)
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Cannot fetch/save image tags", e)
                    }
                }

                db.savedNewsDAO().saveNews(newsItem)

                val similar = try {
                    newsDAO.getSimilarStories(newsItem.uuid).also {
                        it.forEach { db.savedNewsDAO().saveNews(it) }
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Fallback to local similar", e)
                    db.savedNewsDAO().getNewsWithCategory(newsItem.category)
                        .filter { it.uuid != newsItem.uuid }
                        .sortedByDescending { it.publishedDate }
                        .take(2)
                }

                similarCache[newsItem.uuid] = similar
            } finally {
                withContext(Dispatchers.Main) { onComplete() }
            }
        }
    }

    fun getSimilarFromCache(uuid: String): List<NewsItem> =
        similarCache[uuid] ?: emptyList()
}
