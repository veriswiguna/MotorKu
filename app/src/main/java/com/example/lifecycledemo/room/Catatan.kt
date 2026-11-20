package com.example.lifecycledemo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Latihan 4 - Room Entity
 * Representasi 1 baris tabel "catatan" dalam database SQLite
 */
@Entity(tableName = "catatan")
data class Catatan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val isi: String
)
