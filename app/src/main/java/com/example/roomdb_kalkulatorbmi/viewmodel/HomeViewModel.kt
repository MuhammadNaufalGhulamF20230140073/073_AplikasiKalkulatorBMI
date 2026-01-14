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
