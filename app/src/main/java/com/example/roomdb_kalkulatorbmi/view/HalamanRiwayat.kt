package com.example.roomdb_kalkulatorbmi.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.roomdb_kalkulatorbmi.R
import com.example.roomdb_kalkulatorbmi.room.entity.BmiEntity
import com.example.roomdb_kalkulatorbmi.viewmodel.RiwayatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRiwayat(
    userId: Int, // DIDAPAT DARI: Argumen Navigasi (ID User yang dipilih)
    onBack: () -> Unit,
    viewModel: RiwayatViewModel // DIDAPAT DARI: PenyediaViewModel.Factory
) {
    /////// STATE OBSERVATION
    // Mengamati aliran data riwayat BMI berdasarkan userId yang aktif.
    // Menggunakan collectAsState agar UI otomatis update jika ada data dihapus.
    val riwayat by viewModel.riwayat.collectAsState()

    /////// STATE DIALOG (UI CONTROL)
    // Digunakan untuk mengontrol muncul/tidaknya jendela konfirmasi hapus.
    var showDialog by remember { mutableStateOf(false) }
    var itemTerpilih by remember { mutableStateOf<BmiEntity?>(null) }

    ///////////////////////////////////////////////////////////////////////////
    // 1. DIALOG KONFIRMASI HAPUS (PENGAMAN DATA)
    ///////////////////////////////////////////////////////////////////////////
    if (showDialog && itemTerpilih != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.judul_konfirmasi_hapus),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.pesan_konfirmasi_hapus, itemTerpilih!!.tanggal)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // AKSI: Menghapus data dari Database melalui ViewModel
                        itemTerpilih?.let { viewModel.hapus(it) }
                        showDialog = false
                        itemTerpilih = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.tombol_hapus),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(R.string.tombol_batal))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.judul_riwayat),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali)
                        )
                    }
                }
            )
        }
    ) { padding ->

        ///////////////////////////////////////////////////////////////////////////
        // 2. KONDISI TAMPILAN (DATA KOSONG VS ADA DATA)
        ///////////////////////////////////////////////////////////////////////////
        if (riwayat.isEmpty()) {
            // Tampilan jika user ini belum pernah menghitung BMI sama sekali
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.pesan_riwayat_kosong),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Tampilan Daftar Riwayat menggunakan LazyColumn (Scroll Vertikal)
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(riwayat) { bmi ->
                    ///////////////////////////////////////////////////////////////////////////
                    // 3. KARTU RIWAYAT (DATABASE -> UI)
                    ///////////////////////////////////////////////////////////////////////////
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            // INFO: Tanggal (Didapat dari kolom 'tanggal' di tabel bmi)
                            Text(
                                text = "${stringResource(R.string.label_tanggal)} ${bmi.tanggal}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(Modifier.height(4.dp))

                            // INFO: Skor BMI
                            Text(
                                text = "${stringResource(R.string.label_bmi)} ${String.format("%.2f", bmi.nilaiBmi)}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                            )

                            // INFO: Nama Kategori (Kurus/Normal/Dsb)
                            Text(
                                text = "${stringResource(R.string.label_kategori_riwayat)} ${bmi.kategori}",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )

                            // INFO DETAIL: Berat & Tinggi saat perhitungan dilakukan
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "${stringResource(R.string.label_berat_riwayat)} ${bmi.berat} ${stringResource(R.string.satuan_kg)}",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "${stringResource(R.string.label_tinggi_riwayat)} ${bmi.tinggi} ${stringResource(R.string.satuan_cm)}",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            /////// AKSI: TOMBOL HAPUS SATUAN
                            // Menandai item mana yang akan dihapus sebelum memunculkan dialog
                            Button(
                                onClick = {
                                    itemTerpilih = bmi
                                    showDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                ),
                                modifier = Modifier.align(Alignment.End),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.tombol_hapus),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}