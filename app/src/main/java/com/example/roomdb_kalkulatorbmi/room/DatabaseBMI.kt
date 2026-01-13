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

    ///////////////////////////////////////////////////////////////////////////
    // 2. SINGLETON PATTERN (Mencegah Database Terbuka Ganda)
    ///////////////////////////////////////////////////////////////////////////
    companion object {
        @Volatile
        private var INSTANCE: DatabaseBMI? = null

        fun getDatabase(context: Context): DatabaseBMI {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseBMI::class.java,
                    "bmi_database"
                )
                    // Menghapus data lama jika versi database naik (migrasi destruktif)
                    .fallbackToDestructiveMigration()
                    // Memanggil fungsi otomatis saat database pertama kali dibuat
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 3. CALLBACK (Otomatisasi saat Database Pertama Kali Dibuat)
    ///////////////////////////////////////////////////////////////////////////
    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                // Menggunakan Coroutine agar pengisian data awal tidak membuat aplikasi lag
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(database.bmiDao())
                }
            }
        }
    }
}

