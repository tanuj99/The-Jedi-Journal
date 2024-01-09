package com.example.thejedijournal.data.remote.model

import com.example.thejedijournal.data.remote.model.*
import kotlinx.serialization.Serializable

@Serializable
data class RootResponse(
    val count: Int,
    val next: String,
    val previous: String? = null,
    val results: List<CharacterModel>
)