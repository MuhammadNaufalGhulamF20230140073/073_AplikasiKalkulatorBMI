package com.example.roomdb_kalkulatorbmi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.roomdb_kalkulatorbmi.repositori.RepositoryUser
import com.example.roomdb_kalkulatorbmi.room.entity.UserEntity
import kotlinx.coroutines.flow.Flow

///////////////////////////////////////////////////////////////////////////
// 1. DEFINISI KELAS & SUMBER DATA (REPOSITORY)
///////////////////////////////////////////////////////////////////////////
class UtamaViewModel(
    // [repoUser] -> Didapat dari ContainerApp -> RepositoryUser.
    // Variabel ini adalah jembatan untuk mengakses tabel User di Room Database.
    private val repoUser: RepositoryUser
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // 2. FUNGSI PENYEDIA DATA (ALUR: DB -> REPO -> VIEWMODEL -> UI/NAV)
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Fungsi ini bertugas mengambil data profil user secara spesifik.
     * @param userId ID unik milik user yang didapat dari rute navigasi.
     * @return Mengembalikan data dalam bentuk Flow (Otomatis update jika data di DB berubah).
     * * DIDAPAT DARI:
     * - ViewModel memanggil [RepositoryUser.getUserById].
     * - Repository memanggil [UserDao.getUserById].
     * - Dao menjalankan Query: "SELECT * FROM user WHERE userId = :id"
     */
    fun getUserById(userId: Int): Flow<UserEntity?> {
        // Mengirimkan aliran data (Flow) langsung dari database ke PetaNavigasi
        return repoUser.getUserById(userId)
    }
}