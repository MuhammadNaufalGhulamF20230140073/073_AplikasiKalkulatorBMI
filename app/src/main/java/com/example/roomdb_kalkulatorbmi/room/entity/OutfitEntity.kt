package com.example.roomdb_kalkulatorbmi.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfit")
data class OutfitEntity(

    @PrimaryKey(autoGenerate = true)
    val outfitId: Int = 0,

    val kategoriId: Int,      // 🔥 FK ke kategori_bmi
    val deskripsi: String,
    val gambarResId: Int
)

