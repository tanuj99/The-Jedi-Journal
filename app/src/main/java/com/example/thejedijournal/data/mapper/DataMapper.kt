package com.example.thejedijournal.data.mapper

import com.example.thejedijournal.data.*
import com.example.thejedijournal.data.local.*
import com.example.thejedijournal.data.remote.model.*

fun CharacterModel.toCharactersEntity(): CharactersEntity {
    return CharactersEntity(
        eyeColor = eyeColor,
        name = name,
        url = url,
        created = created,
        edited = edited,
        gender = gender,
        birthYear = birthYear,
        films = films
    )
}

fun FilmsModel.toFilmsEntity(): FilmsEntity {
    return FilmsEntity(
        url = url,
        title = title,
        releaseDate = releaseDate
    )
}