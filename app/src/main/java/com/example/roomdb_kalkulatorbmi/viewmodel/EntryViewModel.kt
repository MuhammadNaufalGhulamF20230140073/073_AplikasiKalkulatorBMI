package com.example.roomdb_kalkulatorbmi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.roomdb_kalkulatorbmi.repositori.RepositoryBMI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS & SUMBER DATA
///////////////////////////////////////////////////////////////////////////
class EntryViewModel(
    // Variabel ini didapat dari [AppContainer] -> [RepositoryBMI]
    // Berfungsi sebagai jembatan komunikasi ke database nantinya
    private val repository: RepositoryBMI
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // 2. STATE MANAGEMENT (Penyimpanan Data Sementara di UI)
    ///////////////////////////////////////////////////////////////////////////

    // _tinggi & _berat: State internal untuk menampung ketikan user di TextField
    private val _tinggi = MutableStateFlow("")
    val tinggi: StateFlow<String> = _tinggi.asStateFlow()

    private val _berat = MutableStateFlow("")
    val berat: StateFlow<String> = _berat.asStateFlow()

    // _errorPesan: Digunakan untuk memberi tahu UI jika input tidak valid
    private val _errorPesan = MutableStateFlow<String?>(null)
    val errorPesan: StateFlow<String?> = _errorPesan.asStateFlow()


    ///////////////////////////////////////////////////////////////////////////
    // 3. FUNGSI UPDATE (Sinkronisasi UI ke State)
    ///////////////////////////////////////////////////////////////////////////

    fun updateTinggi(value: String) {
        _tinggi.value = value
        _errorPesan.value = null // Reset error saat user mulai memperbaiki input
    }

    fun updateBerat(value: String) {
        _berat.value = value
        _errorPesan.value = null // Reset error saat user mulai memperbaiki input
    }


    ///////////////////////////////////////////////////////////////////////////
    // 4. LOGIKA VALIDASI (Pengecekan Keamanan Data)
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Fungsi Validasi: Mengecek apakah input masuk akal untuk manusia
     * Menggunakan logika mandiri di dalam ViewModel (Tanpa memanggil DAO/Repo)
     */
    fun isDataLogis(): Boolean {
        val t = _tinggi.value.toFloatOrNull() ?: 0f
        val b = _berat.value.toFloatOrNull() ?: 0f

        return when {
            // Logika validasi: Tinggi badan normal (50cm - 250cm)
            t < 50f || t > 250f -> {
                _errorPesan.value = "Tinggi badan harus antara 50 - 250 cm"
                false
            }
            // Logika validasi: Berat badan normal (2kg - 500kg)
            b < 2f || b > 500f -> {
                _errorPesan.value = "Berat badan harus antara 2 - 500 kg"
                false
            }
            else -> {
                _errorPesan.value = null
                true
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 5. FUNGSI UTAMA (Pintu Gerbang Sebelum Pindah ke Halaman Hasil)
    ///////////////////////////////////////////////////////////////////////////

    fun hitungBmi(userId: Int): Boolean {
        val tinggiCm = _tinggi.value.toFloatOrNull()
        val beratKg = _berat.value.toFloatOrNull()

        /////// CEK KEKOSONGAN DATA
        if (tinggiCm == null || beratKg == null) {
            _errorPesan.value = "Harap isi semua kolom"
            return false
        }

        /////// CEK KEWAJARAN ANGKA (Memanggil fungsi isDataLogis di atas)
        if (!isDataLogis()) {
            return false
        }

        // Jika semua oke, UI akan berpindah halaman ke 'HasilScreen'
        // Data hasil hitungan baru akan disimpan ke DAO lewat 'HasilViewModel'
        return true
    }
}