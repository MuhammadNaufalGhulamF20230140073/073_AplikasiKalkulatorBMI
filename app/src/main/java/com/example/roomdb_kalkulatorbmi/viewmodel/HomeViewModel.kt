package com.example.roomdb_kalkulatorbmi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdb_kalkulatorbmi.repositori.RepositoryUser
import com.example.roomdb_kalkulatorbmi.room.entity.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS & SUMBER DATA (REPOSITORY)
///////////////////////////////////////////////////////////////////////////
class HomeViewModel(
    // [repository] -> Didapat dari ContainerApp -> RepositoryUser.
    // Variabel ini adalah pintu utama untuk mengelola Tabel User.
    private val repository: RepositoryUser
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // 2. DATA STREAM (Aliran Data Real-Time dari Database ke UI)
    ///////////////////////////////////////////////////////////////////////////

    // [userList] -> Didapat dari repository.getAllUser() (Query: SELECT * FROM user).
    // Menggunakan stateIn agar data dari database (Flow) dikonversi menjadi StateFlow
    // yang bisa dibaca oleh UI Compose secara efisien.
    val userList = repository.getAllUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Tetap aktif 5 detik meski layar mati
            initialValue = emptyList() // Data awal kosong sebelum database merespon
        )
}
