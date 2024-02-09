package com.example.gamelista.model
import com.api.igdb.request.IGDBWrapper
import com.api.igdb.request.IGDBWrapper.apiProtoRequest
import com.api.igdb.request.TwitchAuthenticator
import com.api.igdb.utils.Endpoint
import com.example.gamelista.data.model.GameResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GameService {

//    @GET("https://api.rawg.io/api/games?key=567b75b59cd4487780179c4fc37ab73b&page_size=20")
//    suspend fun listApiGames(@Query("page") page: Int): GameResponse
//
//    @GET("https://api.rawg.io/api/games?key=567b75b59cd4487780179c4fc37ab73b&page_size=20")
//    suspend fun searchGames(
//        @Query("search") query: String,
//        @Query("page") page: Int
//    ): GameResponse

    @GET("games/")
    suspend fun listApiGames(
        @Query("fields") fields: String = "*",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): GameResponse // La respuesta será un JSON

    @GET("games/")
    suspend fun searchGames(
        @Query("search") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GameResponse
}


//object RetrofitServiceFactory {
//    private const val BASE_URL = "https://api.igdb.com/v4/"
//    private const val CLIENT_ID = "9ka22ciij2034pew1ovudpz2esookx"
//    private const val AUTHORIZATION_TOKEN = "6tncw4nq67y7oc4ep2qgkpe3q83j5g"
//    fun makeRetrofitService(): GameService {
//        val client = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val request = chain.request().newBuilder()
//                    .addHeader("Client-ID", CLIENT_ID)
//                    .addHeader("Authorization", AUTHORIZATION_TOKEN)
//                    .build()
//                chain.proceed(request)
//            }
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//            .build()
//
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(ScalarsConverterFactory.create()) // Para obtener la respuesta como String
//            .client(client)
//            .build()
//            .create(GameService::class.java)
//    }
//}



//object RetrofitServiceFactory {
//
//    private const val BASE_URL = "https://api.igdb.com/v4/"
//    private const val CLIENT_ID = "9ka22ciij2034pew1ovudpz2esookx"
//    private const val AUTHORIZATION_TOKEN = "6tncw4nq67y7oc4ep2qgkpe3q83j5g"
////    fun makeRetrofitService(): GameService {
////        return Retrofit.Builder()
////            .baseUrl("https://api.rawg.io/api/")
////            .addConverterFactory(GsonConverterFactory.create())
////            .client(
////                OkHttpClient.Builder()
////                    .addInterceptor(
////                        HttpLoggingInterceptor()
////                            .setLevel(HttpLoggingInterceptor.Level.BODY))
////                    .build()
////            )
////            .build()
////            .create(GameService::class.java)
////    }
//
//    fun makeRetrofitService(): GameService {
//        val client = OkHttpClient.Builder()
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//            .build()
//
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(ScalarsConverterFactory.create()) // Para obtener la respuesta como String
//            .client(client)
//            .build()
//            .create(GameService::class.java)
//    }
//
//}