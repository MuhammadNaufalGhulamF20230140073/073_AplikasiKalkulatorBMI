package com.example.roomdb_kalkulatorbmi.repositori

import com.example.roomdb_kalkulatorbmi.room.dao.UserDao
import com.example.roomdb_kalkulatorbmi.room.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class RepositoryUser(
    private val userDao: UserDao
) {

    // Mengambil semua daftar user (untuk Halaman Home)
    fun getAllUser(): Flow<List<UserEntity>> =
        userDao.getAllUser()

    // Mengambil satu user berdasarkan ID (untuk update status aktif)
    fun getUserById(id: Int): Flow<UserEntity?> =
        userDao.getUserById(id)

    // Memasukkan user baru (saat pendaftaran)
    suspend fun insertUser(user: UserEntity) =
        userDao.insertUser(user)

    // 🔥 TAMBAHKAN INI: Untuk memperbarui kolom tanggalDiubah dan Nama
    suspend fun updateUser(user: UserEntity) =
        userDao.updateUser(user)

    // Menghapus profil user
    suspend fun deleteUser(user: UserEntity) =
        userDao.deleteUser(user)
}