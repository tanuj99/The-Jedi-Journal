package com.example.thejedijournal.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RootPeopleResponse(
    val count: Int,
    val next: String,
    val previous: String? = null,
    val results: List<CharacterModel>
)

@Serializable
data class RootFilmsResponse(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<FilmsModel>
)