package com.example.roomdb_kalkulatorbmi.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdb_kalkulatorbmi.repositori.RepositoryBMI
import com.example.roomdb_kalkulatorbmi.room.entity.BmiEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS & SUMBER DATA
///////////////////////////////////////////////////////////////////////////
class RiwayatViewModel(
    // [repository] -> Didapat dari ContainerApp -> RepositoryBMI.
    // Menghubungkan ViewModel ke BmiDao untuk akses tabel 'bmi'.
    private val repository: RepositoryBMI,

    // [savedStateHandle] -> Disediakan oleh Android System.
    // Menyimpan data ID User yang dikirim saat pindah halaman agar tidak hilang.
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // 2. STATE MANAGEMENT (IDENTITAS USER)
    ///////////////////////////////////////////////////////////////////////////

    // [_userId] -> State internal untuk menampung ID User yang sedang dilihat riwayatnya.
    // Nilai awal diambil dari Navigasi (SavedStateHandle), jika kosong jadi 0.
    private val _userId = MutableStateFlow(savedStateHandle.get<Int>("userId") ?: 0)

    ///////////////////////////////////////////////////////////////////////////
    // 3. REACTIVE DATA STREAM (ALUR: DB -> REPO -> UI)
    ///////////////////////////////////////////////////////////////////////////

    // [riwayat] -> Aliran data (Stream) riwayat BMI.
    // [flatMapLatest] -> Logika canggih: Setiap kali _userId berubah,
    // sistem akan otomatis mematikan pencarian lama dan mencari data riwayat milik ID baru tersebut.
    val riwayat: StateFlow<List<BmiEntity>> = _userId.flatMapLatest { id ->
        Log.d("DEBUG_BMI", "ViewModel mencari riwayat untuk UserID: $id")

        // Memanggil fungsi di BmiDao melalui repository
        repository.getRiwayatUser(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList() // Muncul daftar kosong saat data sedang dimuat
    )

    ///////////////////////////////////////////////////////////////////////////
    // 4. FUNGSI KONTROL (SETTER & ACTION)
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Memaksa ViewModel mencari riwayat user lain secara manual.
     */
    fun setUserId(id: Int) {
        _userId.value = id
    }

    /**
     * Menghapus satu baris riwayat BMI tertentu.
     * Alur: Memanggil 'deleteBmi' di BmiDao melalui repository.
     */
    fun hapus(bmi: BmiEntity) {
        viewModelScope.launch {
            repository.hapusRiwayat(bmi)
        }
    }
}