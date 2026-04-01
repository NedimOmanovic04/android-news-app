// path: etf/ri/rma/newsfeedapp/data/network/ImaggaDAO.kt

package etf.ri.rma.newsfeedapp.data.network

import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImaggaDAO {
    private const val BASE_URL = "https://api.imagga.com/"
    private const val API_KEY = "acc_013605db2fbeecb"
    private const val API_SECRET = "aead87716a0aa4f65e6181b4e6c01f6b"

    private var service: ImagaApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ImagaApiService::class.java)
    }

    fun setApiService(imaggaApiService: ImagaApiService) {
        service = imaggaApiService
    }

    private val tagCache = mutableMapOf<String, List<String>>()

    suspend fun getTags(imageURL: String): List<String> {
        if (!imageURL.startsWith("http")) {
            throw InvalidImageURLException("Invalid URL: $imageURL")
        }
        tagCache[imageURL]?.let { return it }

        val auth = Credentials.basic(API_KEY, API_SECRET)
        val resp = service.getTags(imageURL, auth)
        val tags = resp.result.tags.map { it.tag.en }
        tagCache[imageURL] = tags
        return tags
    }
}
