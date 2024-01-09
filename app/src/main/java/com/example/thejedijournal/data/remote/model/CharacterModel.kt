package com.example.thejedijournal.data.remote.model

import kotlinx.serialization.*


@Serializable
data class CharacterModel(

    @SerialName("birth_year")
    val birthYear: String,

    val created: String,
    val edited: String,

    @SerialName("eye_color")
    val eyeColor: String,
    val films: List<String>,
    val gender: String,

    @SerialName("hair_color")
    val hairColor: String,

    val height: String,
    val homeworld: String,
    val mass: String,
    val name: String,

    @SerialName("skin_color")
    val skinColor: String,

    val species: List<String>,
    val starships: List<String>,
    val url: String,
    val vehicles: List<String>
)