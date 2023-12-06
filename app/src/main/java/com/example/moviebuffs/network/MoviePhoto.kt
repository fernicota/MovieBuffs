package com.example.moviebuffs.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePhoto(
val title: String,
@SerialName(value = "poster")
val posterUrl: String,
val description: String,
@SerialName(value = "release_date")
val releaseDate: String,
@SerialName(value = "content_rating")
val contentRating: String,
@SerialName(value = "review_score")
val reviewScore: String,
@SerialName(value = "big_image")
val bigImageUrl: String,
val length: String
)

