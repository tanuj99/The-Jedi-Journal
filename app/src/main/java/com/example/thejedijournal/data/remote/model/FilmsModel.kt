package com.example.thejedijournal.data.remote.model

import kotlinx.serialization.*

@Serializable
data class FilmsModel(
    val characters: List<String>,
    val created: String,
    val director: String,
    val edited: String,

    @SerialName("episode_id")
    val episodeId: Int,

    @SerialName("opening_crawl")
    val openingCrawl: String,

    val planets: List<String>,
    val producer: String,

    @SerialName("release_date")
    val releaseDate: String,

    val species: List<String>,
    val starships: List<String>,
    val title: String,
    val url: String,
    val vehicles: List<String>
)
