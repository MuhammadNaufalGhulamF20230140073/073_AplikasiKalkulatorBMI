package com.example.roomdb_kalkulatorbmi.room.dao

import androidx.room.*
import com.example.roomdb_kalkulatorbmi.room.entity.BmiEntity
import com.example.roomdb_kalkulatorbmi.room.entity.KategoriBmiEntity
import com.example.roomdb_kalkulatorbmi.room.entity.OutfitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BmiDao {

    ///////////////////////////////////////////////////////////////////////////
    // 1. BAGIAN RIWAYAT BMI (Aktivitas User)
    ///////////////////////////////////////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBmi(bmi: BmiEntity)

    // Mengambil semua riwayat berdasarkan ID User tertentu
    @Query("""
        SELECT * FROM bmi 
        WHERE userId = :userId 
        ORDER BY bmiId DESC
    """)
    fun getRiwayatUser(userId: Int): Flow<List<BmiEntity>>

    @Delete
    suspend fun deleteBmi(bmi: BmiEntity)


    ///////////////////////////////////////////////////////////////////////////
    // 2. BAGIAN MASTER KATEGORI (Data Acuan BMI)
    ///////////////////////////////////////////////////////////////////////////

    // Mencari kategori (Kurus/Normal/Gemuk) berdasarkan skor BMI
    @Query("""
        SELECT * FROM kategori_bmi
        WHERE :nilaiBmi >= batasMin
        ORDER BY batasMin DESC
        LIMIT 1
    """)
    suspend fun getKategoriByNilai(nilaiBmi: Float): KategoriBmiEntity?

    // Untuk mengisi data kategori pertama kali (Seed data)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKategori(kategori: List<KategoriBmiEntity>)
