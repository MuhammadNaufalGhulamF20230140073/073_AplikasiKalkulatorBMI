package com.example.roomdb_kalkulatorbmi.view.uicontroller

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roomdb_kalkulatorbmi.view.*
import com.example.roomdb_kalkulatorbmi.view.route.*
import com.example.roomdb_kalkulatorbmi.viewmodel.*

@Composable
fun PetaNavigasi() {
    // [navController] -> Alat utama untuk berpindah antar layar (navigate)
    // dan membawa data (savedStateHandle).
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route
    ) {

        /////// 1. HALAMAN DAFTAR USER (HOME)
        composable(DestinasiHome.route) {
            HalamanHome(
                onLanjut = { user ->
                    // MENYIMPAN: ID user yang diklik ke savedStateHandle agar bisa dipakai di layar berikutnya.
                    navController.currentBackStackEntry?.savedStateHandle?.set("userId", user.userId)
                    navController.navigate(DestinasiUtama.route)
                }
            )
        }

        /////// 2. HALAMAN DASHBOARD (UTAMA)
        composable(DestinasiUtama.route) {
            // DIDAPAT DARI: savedStateHandle milik layar sebelumnya (Home).
            val userId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("userId") ?: 0

            val utamaViewModel: UtamaViewModel = viewModel(factory = PenyediaViewModel.Factory)
            // MENGAMBIL: Data profil lengkap user dari DB berdasarkan ID.
            val userState by utamaViewModel.getUserById(userId).collectAsState(initial = null)
            val namaTampilan = userState?.nama ?: "User $userId"

            HalamanUtama(
                namaUser = namaTampilan,
                onInputBmi = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("userId", userId)
                    navController.navigate(DestinasiEntry.route)
                },
                onRiwayat = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("userId", userId)
                    navController.navigate(DestinasiRiwayat.route)
                },
                onLogout = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiHome.route) { inclusive = true }
                    }
                }
            )
        }

        /////// 3. HALAMAN INPUT DATA (ENTRY)
        composable(DestinasiEntry.route) {
            // DIDAPAT DARI: Dashboard (Utama)
            val userId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("userId") ?: 0
            val entryViewModel: EntryViewModel = viewModel(factory = PenyediaViewModel.Factory)

            HalamanEntry(
                viewModel = entryViewModel,
                userId = userId,
                onBack = { navController.popBackStack() },
                onHitung = { tinggi, berat ->
                    // MENYIMPAN: Data fisik untuk dihitung di layar Hasil.
                    navController.currentBackStackEntry?.savedStateHandle?.set("userId", userId)
                    navController.currentBackStackEntry?.savedStateHandle?.set("tinggi", tinggi)
                    navController.currentBackStackEntry?.savedStateHandle?.set("berat", berat)
                    navController.navigate(DestinasiHasil.route)
                }
            )
        }

        /////// 4. HALAMAN SKOR & SIMPAN (HASIL)
        composable(DestinasiHasil.route) {
            val backStackEntry = navController.previousBackStackEntry
            // DIDAPAT DARI: Entry BMI.
            val userId = backStackEntry?.savedStateHandle?.get<Int>("userId") ?: 0
            val tinggi = backStackEntry?.savedStateHandle?.get<Float>("tinggi") ?: 0f
            val berat = backStackEntry?.savedStateHandle?.get<Float>("berat") ?: 0f

            val hasilViewModel: HasilViewModel = viewModel(factory = PenyediaViewModel.Factory)

            HalamanHasil(
                viewModel = hasilViewModel,
                userId = userId,
                tinggi = tinggi,
                berat = berat,
                onBack = { navController.popBackStack() },
                onLihatOutfit = { kategoriId ->
                    // MENYIMPAN: kategoriId (1-5) untuk menentukan rekomendasi baju.
                    navController.currentBackStackEntry?.savedStateHandle?.set("userId", userId)
                    navController.currentBackStackEntry?.savedStateHandle?.set("kategoriId", kategoriId)
                    navController.navigate(DestinasiOutfit.route)
                }
            )
        }

        /////// 5. HALAMAN REKOMENDASI BAJU (OUTFIT)
        composable(DestinasiOutfit.route) {
            val backStackEntry = navController.previousBackStackEntry
            // DIDAPAT DARI: Hasil BMI.
            val kategoriId = backStackEntry?.savedStateHandle?.get<Int>("kategoriId") ?: 0
            val userId = backStackEntry?.savedStateHandle?.get<Int>("userId") ?: 0

            val outfitViewModel: OutfitViewModel = viewModel(factory = PenyediaViewModel.Factory)

            HalamanOutfit(
                kategoriId = kategoriId,
                onBack = { navController.popBackStack(DestinasiEntry.route, inclusive = false) },
                onSelesai = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("userId", userId)
                    navController.navigate(DestinasiUtama.route) {
                        popUpTo(DestinasiUtama.route) { inclusive = true }
                    }
                },
                viewModel = outfitViewModel
            )
        }

        /////// 6. HALAMAN DAFTAR LAMA (RIWAYAT)
        composable(DestinasiRiwayat.route) {
            // 1. Ambil ID dari layar Dashboard.
            val userIdDariUtama = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("userId") ?: 0

            // 2. Buat ViewModel.
            val riwayatViewModel: RiwayatViewModel = viewModel(factory = PenyediaViewModel.Factory)

            // 3. PAKSA MASUKKAN ID: Karena Riwayat perlu tahu ID siapa yang mau dicari datanya.
            LaunchedEffect(userIdDariUtama) {
                riwayatViewModel.setUserId(userIdDariUtama)
            }

            HalamanRiwayat(
                userId = userIdDariUtama,
                viewModel = riwayatViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}