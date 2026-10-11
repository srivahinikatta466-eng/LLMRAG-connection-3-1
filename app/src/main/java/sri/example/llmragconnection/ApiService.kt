package sri.example.llmragconnection

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("search")
    fun searchDocument(
        @Body request: SearchRequest
    ): Call<SearchResponse>
}