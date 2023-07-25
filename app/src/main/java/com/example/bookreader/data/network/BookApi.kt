package com.example.bookreader.data.network

import com.example.bookreader.models.BookApiModel
import com.example.bookreader.models.DocsApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {

    @GET("/search.json")
    suspend fun getDocsApi(@Query(value = "q") query: String): DocsApiModel

    @GET(value = "/works/{workId}.json")
    suspend fun getBookByWork(@Path(value = "workId") workId :String) : BookApiModel
}