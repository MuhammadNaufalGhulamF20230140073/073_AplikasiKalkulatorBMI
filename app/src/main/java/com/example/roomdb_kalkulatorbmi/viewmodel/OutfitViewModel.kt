package com.example.roomdb_kalkulatorbmi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdb_kalkulatorbmi.repositori.RepositoryBMI
import com.example.roomdb_kalkulatorbmi.room.entity.OutfitEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS & SUMBER DATA (REPOSITORY)
///////////////////////////////////////////////////////////////////////////
class OutfitViewModel(
    // [repository] -> Didapat dari ContainerApp -> RepositoryBMI.
    // Variabel ini digunakan untuk mengakses data Master Outfit di database.
    private val repository: RepositoryBMI
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // 2. UI STATE (Tempat Penyimpanan Daftar Baju)
    ///////////////////////////////////////////////////////////////////////////

    // [_outfitList] -> State internal untuk menampung daftar baju hasil Query.
    // Data ini bertipe List<OutfitEntity> yang berisi deskripsi dan gambar baju.
    private val _outfitList = MutableStateFlow<List<OutfitEntity>>(emptyList())
    val outfitList: StateFlow<List<OutfitEntity>> = _outfitList

    ///////////////////////////////////////////////////////////////////////////
    // 3. FUNGSI LOAD DATA (DIAMBIL DARI DAO -> REPO -> VIEWMODEL)
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Fungsi untuk mengambil daftar outfit berdasarkan kategori BMI.
     * @param kategoriId Didapat dari HasilViewModel (1:Kurus, 2:Normal, 3:Overweight, 4:Obesitas)
     */
    fun loadOutfit(kategoriId: Int) {
        // Menggunakan Coroutine (viewModelScope) karena akses database adalah proses berat
        viewModelScope.launch {
            // [getOutfitByKategori] -> Perintah ini diteruskan ke BmiDao.
            // Dao menjalankan Query: "SELECT * FROM outfit WHERE kategoriId = :kategoriId"
            _outfitList.value = repository.getOutfitByKategori(kategoriId)
        }
    }
}