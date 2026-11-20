package com.example.lifecycledemo.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Latihan 4 - Room Database Abstract Class
 * Penghubung utama antara Room ORM dengan database SQLite lokal HP
 */
@Database(entities = [Catatan::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catatanDao(): CatatanDao
}
