package com.example.roomdb_kalkulatorbmi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdb_kalkulatorbmi.repositori.RepositoryBMI
import com.example.roomdb_kalkulatorbmi.repositori.RepositoryUser
import com.example.roomdb_kalkulatorbmi.room.entity.BmiEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS & SUMBER REPOSITORY
///////////////////////////////////////////////////////////////////////////
class HasilViewModel(
    // [repositoryBmi] -> Didapat dari ContainerApp (database.bmiDao)
    // Digunakan khusus untuk urusan Tabel BMI, Kategori, dan Outfit.
    private val repositoryBmi: RepositoryBMI,

    // [repositoryUser] -> Didapat dari ContainerApp (database.userDao)
    // Digunakan khusus untuk urusan Tabel User (Update status tanggal aktif).
    private val repositoryUser: RepositoryUser
) : ViewModel() {
