package com.example.roomdb_kalkulatorbmi

import android.app.Application
import com.example.roomdb_kalkulatorbmi.repositori.ContainerApp

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS APPLICATION (TITIK AWAL APLIKASI)
///////////////////////////////////////////////////////////////////////////
/**
 * Kelas AplikasiBMI adalah kelas induk yang berjalan sebelum layar apa pun muncul.
 * Di sinilah kita menyiapkan database agar siap digunakan oleh seluruh ViewModel.
 */
class AplikasiBMI : Application() {

    ///////////////////////////////////////////////////////////////////////////
    // 2. PENYIAPAN CONTAINER (PUSAT DATA)
    ///////////////////////////////////////////////////////////////////////////

    // [container] -> Objek tunggal (Singleton) yang menyimpan semua Repository.
    // 'lateinit' berarti variabel ini akan diisi segera saat aplikasi menyala.
    // 'private set' berarti hanya kelas ini yang boleh mengisi, kelas lain hanya boleh membaca.
    lateinit var container: ContainerApp
        private set

    ///////////////////////////////////////////////////////////////////////////
    // 3. PROSES INISIALISASI (SAAT APLIKASI PERTAMA KALI DIBUKA)
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreate() {
        super.onCreate()

        /////// MEMBUAT CONTAINER
        // 'this' merujuk pada Context aplikasi.
        // ContainerApp membutuhkan Context ini untuk membangun Room Database.
        container = ContainerApp(this)
    }
}