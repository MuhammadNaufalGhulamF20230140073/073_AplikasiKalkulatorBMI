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
