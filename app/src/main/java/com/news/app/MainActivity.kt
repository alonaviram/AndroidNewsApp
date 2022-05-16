package com.news.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit =
            Retrofit.Builder()
                .addConverterFactory(Json.asConverterFactory(MediaType.parse("application/json")!!))
                .baseUrl("https://gist.githubusercontent.com/")
                .build();

        val articlesService = retrofit.create(ArticlesService::class.java)
        articlesService.getArticles().enqueue(object : Callback<ServerArticlesResponse> {
            override fun onResponse(
                call: Call<ServerArticlesResponse>,
                response: Response<ServerArticlesResponse>
            ) {
                Log.d("Articles", "${response.body()?.articles?.size}")
            }

            override fun onFailure(call: Call<ServerArticlesResponse>, t: Throwable) {
                Log.e("Fail", "get articles", t)
            }

        })
    }
}

interface ArticlesService {
    @GET("alonaviram/2679dae36f43215743aa1cf9aac7e9f6/raw/f51b99ed2ded93ddb8556e0400232d9f038b341c/articles.json")
    fun getArticles(): Call<ServerArticlesResponse>
}

@Serializable
data class ServerArticlesResponse(
    @SerialName("articles")
    val articles: List<ServerArticle>
)

@Serializable
data class ServerArticle(
    @SerialName("id")
    val id: String,
    @SerialName("source")
    val source: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("article_url")
    val articleUrl: String,
    @SerialName("date")
    val date: Long,
    @SerialName("topicsRanking")
    val topics: Map<String, Float>,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("provider")
    val provider: String,
    @SerialName("view_mode")
    val viewMode: String,
)