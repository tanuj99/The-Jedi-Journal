package com.example.thejedijournal.data.local

import androidx.room.*
import com.example.thejedijournal.data.*

@Entity(tableName = "character_table")
@TypeConverters(StringListConverter::class)
data class CharactersEntity(

    val eyeColor: String,

    @PrimaryKey
    val name: String,

    val url: String,
    val created: String,
    val edited: String,
    val gender: String,
    val birthYear: String,
    val films: List<String>
)
