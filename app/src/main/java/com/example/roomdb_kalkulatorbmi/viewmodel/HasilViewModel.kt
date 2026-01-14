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



    ///////////////////////////////////////////////////////////////////////////
    // 2. UI STATE (Data yang ditampilkan ke layar Hasil)
    ///////////////////////////////////////////////////////////////////////////
    private val _nilaiBmi = MutableStateFlow(0f)
    val nilaiBmi: StateFlow<Float> = _nilaiBmi

    private val _kategori = MutableStateFlow("")
    val kategori: StateFlow<String> = _kategori

    private val _kategoriId = MutableStateFlow(0)
    val kategoriId: StateFlow<Int> = _kategoriId}


///////////////////////////////////////////////////////////////////////////
// 3. FUNGSI LOGIKA TAMPILAN (DARI DAO -> REPO -> VIEWMODEL)
///////////////////////////////////////////////////////////////////////////
fun updateDisplayHanya(bmi: Float) {
    viewModelScope.launch {
        try {
            // [getKategoriByNilaiBmi] -> Memanggil BmiDao melalui RepositoryBMI
            // Hasilnya berupa objek 'KategoriBmiEntity' dari database
            val kategoriEntity = repositoryBmi.getKategoriByNilaiBmi(bmi)

            _nilaiBmi.value = bmi
            _kategori.value = kategoriEntity?.namaKategori ?: "Normal"
            _kategoriId.value = kategoriEntity?.kategoriId ?: 2
        } catch (e: Exception) {
            Log.e("DEBUG_BMI", "Gagal update display: ${e.message}")
        }
    }
}


///////////////////////////////////////////////////////////////////////////
// 4. FUNGSI EKSEKUSI DATABASE (SAVE & UPDATE)
///////////////////////////////////////////////////////////////////////////
fun simpanKeRiwayatPermanen(userId: Int, tinggi: Float, berat: Float, bmi: Float) {
    viewModelScope.launch {
        try {
            val formatTanggal = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())

            /////// A. PROSES SIMPAN KE TABEL RIWAYAT (BmiDao)
            // Memanggil 'insertBmi' di BmiDao melalui repositoryBmi
            repositoryBmi.insertBmi(
                BmiEntity(
                    userId = userId,
                    tinggi = tinggi,
                    berat = berat,
                    nilaiBmi = bmi,
                    kategori = _kategori.value,
                    tanggal = formatTanggal
                )
            )

            /////// B. PROSES UPDATE STATUS AKTIF KE TABEL USER (UserDao)
            // 1. Ambil data user dari UserDao menggunakan 'firstOrNull()' agar tidak perlu loop terus-menerus
            val user = repositoryUser.getUserById(userId).firstOrNull()

            if (user != null) {
                // 2. Lakukan 'copy' data lama dan ubah hanya bagian 'tanggalDiubah'
                val userTerupdate = user.copy(tanggalDiubah = formatTanggal)

                // 3. Panggil 'updateUser' di UserDao melalui repositoryUser
                repositoryUser.updateUser(userTerupdate)
                Log.d("DEBUG_BMI", "✅ USER AKTIF: ${user.nama} diperbarui")
            }

            Log.d("DEBUG_BMI", "✅ BERHASIL SIMPAN")
        } catch (e: Exception) {
            Log.e("DEBUG_BMI", "Gagal simpan: ${e.message}")
        }
    }
}
}
