package com.example.lifecycledemo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Latihan 4 - Room DAO (Data Access Object)
 * Menampung fungsi perintah SQL simpan, baca, dan hapus dengan coroutine suspend
 */
@Dao
interface CatatanDao {
    @Insert
    suspend fun tambah(catatan: Catatan)

    @Query("SELECT * FROM catatan ORDER BY id DESC")
    suspend fun semua(): List<Catatan>
}
