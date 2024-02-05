package com.example.gamelista.data.model

import com.google.gson.annotations.SerializedName

data class Game(
    val added: Int,
    val added_by_status: AddedByStatus,
    @SerializedName("background_image") val backgroundImage: String?,
    val clip: Any,
    val dominant_color: String,
    val esrb_rating: Any,
    val genres: List<Genre>,
    val id: Int,
    val metacritic: Int?,
    val name: String,
    val parent_platforms: List<ParentPlatform>,
    val platforms: List<PlatformX>?,
    val playtime: Int,
    val rating: Double,
    val rating_top: Int,
    val ratings: List<Rating>,
    val ratings_count: Int,
    val released: String,
    val reviews_count: Int,
    val reviews_text_count: Int,
    val saturated_color: String,
    val score: String,
    val short_screenshots: List<ShortScreenshot>,
    val slug: String,
    val stores: List<Store>,
    val suggestions_count: Int,
    val tags: List<Tag>,
    val tba: Boolean,
    val updated: String,
    val user_game: Any
)

data class AddedByStatus(
    val beaten: Int,
    val dropped: Int,
    val owned: Int,
    val playing: Int,
    val toplay: Int,
    val yet: Int
)

data class Genre(
    val id: Int,
    val name: String,
    val slug: String
)

data class ParentPlatform(
    val platform: Platform
)

data class PlatformX(
    val platform: Platform
)

data class Rating(
    val count: Int,
    val id: Int,
    val percent: Double,
    val title: String
)

data class ShortScreenshot(
    val id: Int,
    val image: String
)

data class Store(
    val store: StoreX
)

data class Tag(
    val games_count: Int,
    val id: Int,
    val image_background: String,
    val language: String,
    val name: String,
    val slug: String
)

data class Platform(
    val id: Int,
    val name: String?,
    val slug: String
)

data class StoreX(
    val id: Int,
    val name: String,
    val slug: String
)