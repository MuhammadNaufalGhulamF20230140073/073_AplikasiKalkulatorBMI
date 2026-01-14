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
}
