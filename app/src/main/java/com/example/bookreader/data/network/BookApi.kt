package com.example.bookreader.data.network

import com.example.bookreader.models.BookApiModel
import com.example.bookreader.models.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {

    @GET("/books/v1/volumes")
    suspend fun getBookApiModel(@Query(value = "q") query: String): BookApiModel

    @GET(value = "/books/v1/volumes/{bookId}")
    suspend fun getBookById(@Path(value = "bookId") bookId :String) : Item
}