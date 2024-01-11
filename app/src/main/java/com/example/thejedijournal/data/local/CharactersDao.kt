package com.example.thejedijournal.data.local

import androidx.room.*

@Dao
interface CharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterList(
        charactersEntity: List<CharactersEntity>
    )

    @Query("SELECT * FROM character_table ORDER BY name")
    suspend fun getCharacters(): List<CharactersEntity>

    @Query("DELETE FROM character_table")
    suspend fun clearCharactersList()
}