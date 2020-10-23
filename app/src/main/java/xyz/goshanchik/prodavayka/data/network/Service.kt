package xyz.goshanchik.prodavayka.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProdavaykaService {
    @GET("Category")
    suspend fun getCategories(): NetworkCategoryContainer

    @GET("Category/{id}/Product")
    suspend fun getProducts(@Path("id") id: Int): NetworkProductContainer
}


/**
 * Main entry point for network access. Call like `ProdavaykaNetwork.dao.getCategories()`
 */
object ProdavaykaNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://5f82458906957200164333c9.mockapi.io/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val dao = retrofit.create(ProdavaykaService::class.java)
}
