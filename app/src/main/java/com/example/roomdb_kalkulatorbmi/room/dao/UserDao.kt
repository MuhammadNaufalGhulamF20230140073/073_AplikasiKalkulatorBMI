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


    ///////////////////////////////////////////////////////////////////////////
    // 2. BAGIAN MANIPULASI DATA (Tambah, Ubah, Hapus)
    ///////////////////////////////////////////////////////////////////////////

    // Menambah user baru saat klik tombol "Daftar" di Halaman Home
    // OnConflictStrategy.REPLACE artinya jika ID sama, data lama akan ditimpa
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // 🔥 PENTING: Untuk memperbarui data user yang sudah ada
    // Digunakan untuk mengubah 'tanggalDiubah' setiap kali user selesai hitung BMI
    @Update
    suspend fun updateUser(user: UserEntity)

    // Menghapus profil user secara permanen
    @Delete
    suspend fun deleteUser(user: UserEntity)
}