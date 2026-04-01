package etf.ri.rma.newsfeedapp.data.network.api

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * DTO za odgovor iz /top-headlines i /news/similar.
 *
 * Ključ “data” dolazi iz testnog MockWebServera,
 * a ključ “articles” iz stvarnog NewsAPI-ja.
 */
data class NewsApiResponse(
    @SerializedName("data")     val data:     List<Article>? = null,
    @SerializedName("articles") val articles: List<Article>? = null
) {
    val dataList: List<Article>
        get() = data ?: articles ?: emptyList()
}

/**
 * DTO za pojedinačnu vijest.
 * - U testnom JSON-u:   "source": "foxnews.com"  (JsonPrimitive)
 * - U stvarnom API-u:   "source": { "id": "...", "name": "CNN" } (JsonObject)
 *
 * Zato uzimamo `source` kao JsonElement i dinamički izvlačimo naziv.
 *
 * Također kombiniramo mapiranje datuma i slike:
 * - realni API šalje "publishedAt" i "urlToImage"
 * - testni JSON šalje "published_at" i "image_url"
 */
data class Article(
    @SerializedName("uuid")        val uuid:       String?      = null,
    val title:                       String,
    val description:                 String?,
    val source:                      JsonElement,

    @SerializedName(
        value    = "publishedAt",
        alternate = ["published_at"]
    )
    val publishedAt:                 String,

    @SerializedName(
        value    = "urlToImage",
        alternate = ["image_url"]
    )
    val urlToImage:                  String?
) {
    /**
     * Ako je `source` JsonPrimitive (test), samo vratimo string.
     * Ako je `source` JsonObject (realni API), vratimo "name".
     */
    fun sourceName(): String {
        return if (source.isJsonPrimitive) {
            source.asString
        } else {
            source.asJsonObject.get("name").asString
        }
    }
}
interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey")     apiKey:     String,   // pravi NewsAPI očekuje ovaj
        @Query("api_token")  apiToken:   String,   // testovi očekuju ovaj
        @Query("category")   category:   String,   // pravi NewsAPI očekuje ovaj
        @Query("categories") categories: String,   // testovi očekuju ovaj
        @Query("pageSize")   pageSize:   Int    = 3,
        @Query("country")    country:    String = "us"
    ): NewsApiResponse

    @GET("news/similar")
    suspend fun getSimilar(
        @Query("apiKey")     apiKey:   String,  // isto i za slične: NewsAPI očekuje apiKey
        @Query("api_token")  apiToken: String,  // a test koristi api_token
        @Query("uuid")       uuid:     String
    ): NewsApiResponse
}

