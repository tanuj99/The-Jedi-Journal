package com.example.thejedijournal.data.local

import android.content.*
import androidx.room.*

private const val DB_NAME = "characters_database"
@Database(
    entities = [CharactersEntity::class],
    version = 1
)
abstract class CharactersDatabase : RoomDatabase() {
    abstract val dao: CharactersDao

    companion object {
        fun create(context: Context): CharactersDatabase {
            return Room.databaseBuilder(
                context,
                CharactersDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}