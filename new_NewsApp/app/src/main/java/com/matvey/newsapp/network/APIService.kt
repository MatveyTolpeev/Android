package com.matvey.newsapp.network


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface APIService {

//    @GET("top-headlines?q=techcrunc&apiKey=22b06b0e837944ddb7269c540b08c2db")
//    suspend fun getNews() : NewsOp
    
    @GET("everything?q=a&pageSize=100&sortBy=popularity&apiKey=22b06b0e837944ddb7269c540b08c2db")
    suspend fun getNews() : NewsOp

    @GET("everything")
    suspend fun getNews(@Query("q") query: String = "a",
                        @Query("sortBy") sortBy: String = SORT_BY,
                        @Query("pageSize") pageSize: String = PAGE_SIZE,
                        @Query("apiKey") apiKey: String = API_KEY) : NewsOp


    companion object Factory {
        const val API_KEY = "22b06b0e837944ddb7269c540b08c2db"
        const val SORT_BY = "popularity"
        const val PAGE_SIZE = "100"

        fun create(): APIService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsapi.org/v2/")
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}