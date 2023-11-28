package com.example.gamelista.model

import com.example.gamelista.data.model.GameResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GameService {

    @GET("https://api.rawg.io/api/games?key=567b75b59cd4487780179c4fc37ab73b&page_size=100")
    suspend fun listApiGames(): GameResponse

    @GET("https://api.rawg.io/api/games?key=567b75b59cd4487780179c4fc37ab73b&page_size=100")
    suspend fun searchGames(
        @Query("search") query: String
    ): GameResponse
}

object RetrofitServiceFactory {
    fun makeRetrofitService(): GameService {
        return Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameService::class.java)
    }
}