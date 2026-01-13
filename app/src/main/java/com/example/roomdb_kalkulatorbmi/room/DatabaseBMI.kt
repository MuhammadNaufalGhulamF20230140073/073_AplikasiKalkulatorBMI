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

///////////////////////////////////////////////////////////////////////////
// 4. PREPOPULATE (Mengisi Data Master/Bawaan Secara Otomatis)
///////////////////////////////////////////////////////////////////////////
suspend fun prepopulateDatabase(dao: BmiDao) {

    /////// MENGISI DATA KATEGORI (Acuan Klasifikasi BMI)
    dao.insertKategori(
        listOf(
            KategoriBmiEntity(1, "Kurus", 0f, 18.4f),
            KategoriBmiEntity(2, "Normal", 18.5f, 24.9f),
            KategoriBmiEntity(3, "Overweight", 25.0f, 29.9f),
            KategoriBmiEntity(4, "Obesitas", 30.0f, 500f)
        )
    )

    /////// MENGISI DATA OUTFIT (Rekomendasi Pakaian Berdasarkan Kategori)
    val daftarOutfit = mutableListOf<OutfitEntity>()

    // --- KATEGORI KURUS (ID: 1) ---
    daftarOutfit.add(OutfitEntity(kategoriId = 1, deskripsi = "Gunakan pakaian berlapis (layering) agar tubuh terlihat lebih berisi", gambarResId = R.drawable.kurus_1))
    daftarOutfit.add(OutfitEntity(kategoriId = 1, deskripsi = "Pilih warna terang dan motif horizontal untuk menambah volume visual", gambarResId = R.drawable.kurus_2))
    daftarOutfit.add(OutfitEntity(kategoriId = 1, deskripsi = "Jaket bomber atau parka sangat cocok untuk memberi kesan bahu lebih lebar", gambarResId = R.drawable.kurus_3))
    daftarOutfit.add(OutfitEntity(kategoriId = 1, deskripsi = "Gunakan celana bahan chino atau cargo daripada jeans yang terlalu ketat", gambarResId = R.drawable.kurus_4))

    // --- KATEGORI NORMAL (ID: 2) ---
    daftarOutfit.add(OutfitEntity(kategoriId = 2, deskripsi = "Semua jenis outfit cocok, cobalah gaya Casual Smart", gambarResId = R.drawable.normal_1))
    daftarOutfit.add(OutfitEntity(kategoriId = 2, deskripsi = "Gunakan kemeja slim fit untuk menonjolkan bentuk tubuh yang atletis", gambarResId = R.drawable.normal_2))
    daftarOutfit.add(OutfitEntity(kategoriId = 2, deskripsi = "Kaos polos dengan celana denim selalu menjadi pilihan yang aman dan stylish", gambarResId = R.drawable.normal_3))
    daftarOutfit.add(OutfitEntity(kategoriId = 2, deskripsi = "Eksperimen dengan warna-warna earthy tone untuk tampilan lebih segar", gambarResId = R.drawable.normal_4))

    // --- KATEGORI OVERWEIGHT (ID: 3) ---
    daftarOutfit.add(OutfitEntity(kategoriId = 3, deskripsi = "Pilih pakaian dengan potongan regular fit (tidak terlalu ketat/longgar)", gambarResId = R.drawable.over_1))
    daftarOutfit.add(OutfitEntity(kategoriId = 3, deskripsi = "Gunakan warna-warna gelap seperti Navy atau Charcoal untuk kesan lebih ramping", gambarResId = R.drawable.over_2))
    daftarOutfit.add(OutfitEntity(kategoriId = 3, deskripsi = "Motif garis vertikal kecil membantu memberikan ilusi tubuh lebih tinggi", gambarResId = R.drawable.over_3))
    daftarOutfit.add(OutfitEntity(kategoriId = 3, deskripsi = "Outer seperti blazer atau kardigan tanpa kancing bisa menyamarkan lekuk tubuh", gambarResId = R.drawable.over_4))

    // --- KATEGORI OBESITAS (ID: 4) ---
    daftarOutfit.add(OutfitEntity(kategoriId = 4, deskripsi = "Pilih pakaian berbahan ringan dan menyerap keringat agar nyaman beraktivitas", gambarResId = R.drawable.obes_1))
    daftarOutfit.add(OutfitEntity(kategoriId = 4, deskripsi = "Gunakan pakaian monokromatik (satu warna) dari atas ke bawah", gambarResId = R.drawable.obes_2))
    daftarOutfit.add(OutfitEntity(kategoriId = 4, deskripsi = "Hindari motif yang terlalu besar dan ramai", gambarResId = R.drawable.obes_3))
    daftarOutfit.add(OutfitEntity(kategoriId = 4, deskripsi = "Celana dengan potongan straight cut membantu menyeimbangkan proporsi tubuh", gambarResId = R.drawable.obes_4))

    /////// FINISHER: Memasukkan semua daftar outfit ke database sekaligus
    dao.insertOutfit(daftarOutfit)
}
