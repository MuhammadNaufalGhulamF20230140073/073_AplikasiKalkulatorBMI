package com.example.roomdb_kalkulatorbmi.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,

    val nama: String,

    val tanggalDibuat: String,

    val tanggalDiubah: String
)
