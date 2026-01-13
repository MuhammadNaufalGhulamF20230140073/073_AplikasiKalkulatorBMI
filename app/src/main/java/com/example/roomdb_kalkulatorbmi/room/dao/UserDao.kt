package com.example.roomdb_kalkulatorbmi.room.dao

import androidx.room.*
import com.example.roomdb_kalkulatorbmi.room.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    ///////////////////////////////////////////////////////////////////////////
    // 1. BAGIAN PENCARIAN & DAFTAR USER
    ///////////////////////////////////////////////////////////////////////////

    // Mencari data 1 user spesifik berdasarkan ID-nya
    // Dipakai saat update status "Active" di HasilViewModel
    @Query("SELECT * FROM user WHERE userId = :id")
    fun getUserById(id: Int): Flow<UserEntity?>

    // Mengambil SEMUA daftar user untuk ditampilkan di Halaman Home
    // Diurutkan berdasarkan 'tanggalDiubah' agar user yang baru aktif muncul di paling atas
    @Query("SELECT * FROM user ORDER BY tanggalDiubah DESC")
    fun getAllUser(): Flow<List<UserEntity>>

