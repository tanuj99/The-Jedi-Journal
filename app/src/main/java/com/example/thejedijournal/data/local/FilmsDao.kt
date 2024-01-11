package com.example.thejedijournal.data.local

import androidx.room.*

@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmsList(
        filmsEntity: List<FilmsEntity>
    )

    @Query("SELECT * FROM films_table ORDER BY url")
    suspend fun getFilms(): List<FilmsEntity>

    @Query("DELETE FROM films_table")
    suspend fun clearCharactersList()
}