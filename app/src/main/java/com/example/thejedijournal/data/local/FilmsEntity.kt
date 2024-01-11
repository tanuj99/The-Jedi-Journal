package com.example.thejedijournal.data.local

import androidx.room.*
import com.example.thejedijournal.data.*

@Entity(tableName = "films_table")
@TypeConverters(StringListConverter::class)
data class FilmsEntity(

    val title: String,
    val releaseDate: String,

    @PrimaryKey
    val url: String
)
