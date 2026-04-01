// path: etf/ri/rma/newsfeedapp/data/network/api/ImagaApiService.kt

package etf.ri.rma.newsfeedapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class TagResponse(
    val result: TagResult
)

data class TagResult(
    val tags: List<Tag>
)

data class Tag(
    val tag: TagDetail
)

data class TagDetail(
    val en: String
)

interface ImagaApiService {
    @GET("v2/tags")
    suspend fun getTags(
        @Query("image_url") imageUrl: String,
        @Header("Authorization") authHeader: String
    ): TagResponse
}
