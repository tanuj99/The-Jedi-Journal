package com.example.thejedijournal.data.local

import android.content.*
import androidx.room.*

private const val DB_NAME = "films_database"
@Database(
    entities = [FilmsEntity::class],
    version = 1
)
abstract class FilmsDatabase : RoomDatabase() {
    abstract val dao: FilmsDao

    companion object {
        fun create(context: Context): FilmsDatabase {
            return Room.databaseBuilder(
                context,
                FilmsDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}