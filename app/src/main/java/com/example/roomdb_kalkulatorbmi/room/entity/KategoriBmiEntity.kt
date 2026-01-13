package com.example.roomdb_kalkulatorbmi.room.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategori_bmi")
data class KategoriBmiEntity(

    @PrimaryKey(autoGenerate = true)
    val kategoriId: Int = 0,

    val namaKategori: String,    // Kurus / Normal / Overweight / Obesitas
    val batasMin: Float,         // batas bawah BMI
    val batasMax: Float          // batas atas BMI
)