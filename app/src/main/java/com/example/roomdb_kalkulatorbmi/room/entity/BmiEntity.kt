package com.example.roomdb_kalkulatorbmi.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bmi",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class BmiEntity(

    @PrimaryKey(autoGenerate = true)
    val bmiId: Int = 0,

    val userId: Int,        // 🔥 INI KUNCI
    val tinggi: Float,
    val berat: Float,
    val nilaiBmi: Float,
    val kategori: String,
    val tanggal: String
)

