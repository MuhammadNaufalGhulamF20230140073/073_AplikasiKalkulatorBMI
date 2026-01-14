package com.example.roomdb_kalkulatorbmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.roomdb_kalkulatorbmi.ui.theme.Roomdb_kalkulatorbmiTheme
import com.example.roomdb_kalkulatorbmi.view.uicontroller.PetaNavigasi

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS UTAMA (PINTU MASUK TAMPILAN)
///////////////////////////////////////////////////////////////////////////
/**
 * MainActivity adalah aktivitas tunggal (Single Activity) yang menjadi wadah
 * bagi seluruh tampilan Compose (Screen) di aplikasi ini.
 */
class MainActivity : ComponentActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // 2. SIKLUS HIDUP (ONCREATE)
    ///////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /////// MENGAKTIFKAN UI COMPOSE
        // setContent bertugas mengubah kode Kotlin menjadi tampilan visual di layar HP.
        setContent {

            /////// A. THEME (GAYA VISUAL)
            // Membungkus aplikasi dengan tema warna, tipografi, dan bentuk
            // yang didefinisikan di folder 'ui.theme'.
            Roomdb_kalkulatorbmiTheme {

                /////// B. NAVIGASI (PENGATUR HALAMAN)
                // Memanggil 'PetaNavigasi' yang mengatur perpindahan antar layar.
                // DIDAPAT DARI: File navigasi yang menggunakan 'NavHost'.
                PetaNavigasi()

            }
        }
    }
}