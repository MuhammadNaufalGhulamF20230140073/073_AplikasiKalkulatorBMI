package com.example.roomdb_kalkulatorbmi.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.roomdb_kalkulatorbmi.AplikasiBMI

///////////////////////////////////////////////////////////////////////////
// 1. OBJECT FACTORY (Pusat Pembuatan ViewModel)
///////////////////////////////////////////////////////////////////////////
object PenyediaViewModel {

    // [Factory] -> Bertugas membuat instance (objek) dari setiap ViewModel.
    // Tanpa Factory, ViewModel tidak bisa menerima 'Repository' di constructor-nya.
    val Factory = viewModelFactory {

        /////// A. HOME VIEWMODEL
        // Mengambil 'repoUser' dari Container untuk mengelola daftar nama user.
        initializer {
            HomeViewModel(aplikasiBMI().container.repoUser)
        }

        /////// B. UTAMA VIEWMODEL
        // Digunakan oleh Navigasi untuk memantau siapa User yang sedang login.
        initializer {
            UtamaViewModel(aplikasiBMI().container.repoUser)
        }

        /////// C. ENTRY VIEWMODEL
        // Mengambil 'repoBMI' untuk urusan input tinggi dan berat badan.
        initializer {
            EntryViewModel(aplikasiBMI().container.repoBMI)
        }

        /////// D. HASIL VIEWMODEL (Menerima 2 Repository)
        // [repoBMI] -> Untuk hitung & simpan skor BMI.
        // [repoUser] -> Untuk update tanggal aktif terakhir user.
        initializer {
            HasilViewModel(
                repositoryBmi = aplikasiBMI().container.repoBMI,
                repositoryUser = aplikasiBMI().container.repoUser
            )
        }

        /////// E. OUTFIT VIEWMODEL
        // Mengambil 'repoBMI' untuk menarik data rekomendasi baju dari database.
        initializer {
            OutfitViewModel(aplikasiBMI().container.repoBMI)
        }

        /////// F. RIWAYAT VIEWMODEL (Menerima Repository & SavedState)
        // [savedStateHandle] -> Menangkap ID User yang dikirim lewat rute navigasi.
        initializer {
            val savedStateHandle = this.createSavedStateHandle()
            RiwayatViewModel(
                repository = aplikasiBMI().container.repoBMI,
                savedStateHandle = savedStateHandle
            )
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// 2. FUNGSI EKSTENSI (Pintu Masuk ke Container)
///////////////////////////////////////////////////////////////////////////

/**
 * [aplikasiBMI] -> Fungsi pembantu (Helper) untuk menjangkau 'AppContainer'.
 * DIDAPAT DARI:
 * - Mengambil konteks 'APPLICATION_KEY' dari sistem Android.
 * - Menghubungkannya ke kelas [AplikasiBMI] agar kita bisa memanggil .container.repoBMI
 */
fun CreationExtras.aplikasiBMI(): AplikasiBMI =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiBMI)