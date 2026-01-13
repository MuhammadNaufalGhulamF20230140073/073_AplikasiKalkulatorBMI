package com.example.roomdb_kalkulatorbmi.repositori

import com.example.roomdb_kalkulatorbmi.room.dao.BmiDao
import com.example.roomdb_kalkulatorbmi.room.entity.BmiEntity
import com.example.roomdb_kalkulatorbmi.room.entity.KategoriBmiEntity
import com.example.roomdb_kalkulatorbmi.room.entity.OutfitEntity
import kotlinx.coroutines.flow.Flow
import com.example.roomdb_kalkulatorbmi.room.dao.UserDao // 🔥 Tambahkan ini
import com.example.roomdb_kalkulatorbmi.room.entity.UserEntity

class RepositoryBMI(
    private val bmiDao: BmiDao,
) {


    // ✅ DIPERBAIKI: Nama fungsi di DAO adalah getRiwayatUser, bukan getRiwayatByUser
    fun getRiwayatUser(userId: Int): Flow<List<BmiEntity>> {
        return bmiDao.getRiwayatUser(userId)
    }

    // Menambah data riwayat BMI baru
    suspend fun insertBmi(bmi: BmiEntity) {
        bmiDao.insertBmi(bmi)
    }

    // Menghapus data riwayat BMI
    suspend fun hapusRiwayat(bmi: BmiEntity) {
        bmiDao.deleteBmi(bmi)
    }

    // Di dalam RepositoryBMI.kt
    suspend fun getKategoriByNilaiBmi(bmi: Float): KategoriBmiEntity? {
        return bmiDao.getKategoriByNilai(bmi)
    }



    // Mengambil daftar rekomendasi baju berdasarkan ID kategori
    suspend fun getOutfitByKategori(kategoriId: Int): List<OutfitEntity> {
        return bmiDao.getOutfitByKategori(kategoriId)
    }
}