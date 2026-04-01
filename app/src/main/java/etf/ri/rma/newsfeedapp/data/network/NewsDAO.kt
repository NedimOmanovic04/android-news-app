package etf.ri.rma.newsfeedapp.data.network

import etf.ri.rma.newsfeedapp.data.network.api.Article
import etf.ri.rma.newsfeedapp.data.network.api.NewsApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import etf.ri.rma.newsfeedapp.model.NewsItem
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.util.UUID

object NewsDAO {
    private const val BASE_URL = "https://newsapi.org/v2/"
    private const val API_KEY  = "8e3f2d6bcbdb457a89810e3c1e67492b"

    private var service: NewsApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(NewsApiService::class.java)
    }

    fun setApiService(newsApiService: NewsApiService) {
        service = newsApiService
        allNews.clear()
        categoryCache.clear()
        similarCache.clear()
    }

    private val allNews       = mutableListOf<NewsItem>()
    private val categoryCache = mutableMapOf<String, Pair<Long, List<NewsItem>>>()
    private val similarCache  = mutableMapOf<String, List<NewsItem>>()

    suspend fun getTopStoriesByCategory(category: String): List<NewsItem> {
        val key = category.lowercase()
        val now = Instant.now().toEpochMilli()

        categoryCache[key]?.let { (timestamp, list) ->
            if (now - timestamp < 30_000) {
                return list
            }
        }

        val response = service.getTopHeadlines(
            apiKey     = API_KEY,
            apiToken   = API_KEY,
            category   = key,
            categories = key,
            pageSize   = 3,
            country    = "us"
        )

        val fresh = response.dataList.mapNotNull { art ->
            articleToNewsItem(art, category = key, isFeatured = true)
        }

        allNews.removeAll { it.category == key && it.isFeatured }
        allNews.addAll(0, fresh)

        val combined = allNews.filter { it.category == key }
        categoryCache[key] = now to combined
        return combined
    }

    fun getAllStories(): List<NewsItem> = allNews.toList()

    suspend fun getSimilarStories(uuid: String): List<NewsItem> {
        try {
            UUID.fromString(uuid)
        } catch (e: Exception) {
            throw InvalidUUIDException("$uuid is not a valid UUID")
        }

        similarCache[uuid]?.let { return it }

        val response = service.getSimilar(
            apiKey   = API_KEY,
            apiToken = API_KEY,
            uuid     = uuid
        )

        val fetched = response.dataList.mapNotNull { art ->
            articleToNewsItem(art, category = "general", isFeatured = false)
        }

        fetched.forEach { item ->
            if (allNews.none { it.uuid == item.uuid }) {
                allNews.add(item)
            }
        }

        val originCategory = allNews.firstOrNull { it.uuid == uuid }?.category
            ?: fetched.firstOrNull()?.category
            ?: "general"

        val similar = allNews
            .filter { it.category == originCategory && it.uuid != uuid }
            .sortedByDescending { it.publishedDate }
            .take(2)

        similarCache[uuid] = similar
        return similar
    }

    private fun articleToNewsItem(
        article:    Article,
        category:   String,
        isFeatured: Boolean
    ): NewsItem? {
        // fallback na stabilan UUID baziran na title + publishedAt
        val uuid = article.uuid
            ?: UUID.nameUUIDFromBytes("${article.title}${article.publishedAt}".toByteArray())
                .toString()

        return article.urlToImage?.let { imageUrl ->
            NewsItem(
                uuid          = uuid,
                title         = article.title,
                snippet       = article.description.orEmpty(),
                source        = article.sourceName(),
                publishedDate = article.publishedAt.substring(0, 10),
                category      = category,
                isFeatured    = isFeatured,
                imageUrl      = imageUrl
            )
        }
    }

    fun addInitialNewsItem(item: NewsItem) {
        if (allNews.none { it.uuid == item.uuid }) {
            allNews.add(item)
        }
    }
}
