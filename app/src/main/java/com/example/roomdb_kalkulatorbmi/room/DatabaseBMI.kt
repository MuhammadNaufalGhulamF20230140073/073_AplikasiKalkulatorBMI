package com.example.roomdb_kalkulatorbmi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomdb_kalkulatorbmi.R
import com.example.roomdb_kalkulatorbmi.room.dao.BmiDao
import com.example.roomdb_kalkulatorbmi.room.dao.UserDao
import com.example.roomdb_kalkulatorbmi.room.entity.BmiEntity
import com.example.roomdb_kalkulatorbmi.room.entity.KategoriBmiEntity
import com.example.roomdb_kalkulatorbmi.room.entity.OutfitEntity
import com.example.roomdb_kalkulatorbmi.room.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

///////////////////////////////////////////////////////////////////////////
// 1. KONFIGURASI DATABASE (Daftar Tabel & Versi)
///////////////////////////////////////////////////////////////////////////
@Database(
    entities = [
        UserEntity::class,
        BmiEntity::class,
        KategoriBmiEntity::class,
        OutfitEntity::class
    ],
    version = 3, // Jika kamu menambah kolom di Entity, naikkan nomor versi ini
    exportSchema = false
)
abstract class DatabaseBMI : RoomDatabase() {

    // Menghubungkan Database dengan perintah SQL di DAO
    abstract fun bmiDao(): BmiDao
    abstract fun userDao(): UserDao