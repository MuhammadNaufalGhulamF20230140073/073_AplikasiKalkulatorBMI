package com.example.roomdb_kalkulatorbmi.repositori

import android.content.Context
import com.example.roomdb_kalkulatorbmi.room.DatabaseBMI

class ContainerApp(context: Context) {

    // Inisialisasi Database
    private val database: DatabaseBMI by lazy {
        DatabaseBMI.getDatabase(context)
    }

    // Pastikan nama variabel ini sesuai dengan yang dipanggil di PenyediaViewModel

    // 1. Repository untuk BMI (Gunakan repoBMI agar singkat)
    val repoBMI: RepositoryBMI by lazy {
        RepositoryBMI(database.bmiDao())
    }

    // 2. Repository untuk User (Tambahkan ini agar tidak merah lagi)
    val repoUser: RepositoryUser by lazy {
        RepositoryUser(database.userDao())
    }
}