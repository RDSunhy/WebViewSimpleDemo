package com.sunhy.demo.base.http

import com.sunhy.demo.base.entity.BaseEntity
import com.sunhy.demo.base.entity.News
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import retrofit2.http.Url

interface WebApiService {
    @GET
    suspend fun download(
        @Url url: String = "",
        @HeaderMap header: Map<String, String>
    ): ResponseBody

    @GET
    suspend fun getNewsList(@Url url: String): BaseEntity<News>

}
