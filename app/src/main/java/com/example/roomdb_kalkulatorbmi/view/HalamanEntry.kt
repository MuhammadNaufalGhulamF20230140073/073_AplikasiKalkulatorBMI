package com.example.roomdb_kalkulatorbmi.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomdb_kalkulatorbmi.R
import com.example.roomdb_kalkulatorbmi.viewmodel.EntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntry(
    viewModel: EntryViewModel,
    userId: Int,
    onBack: () -> Unit,
    onHitung: (Float, Float) -> Unit
) {
    // Observasi state dari ViewModel
    val tinggi by viewModel.tinggi.collectAsState()
    val berat by viewModel.berat.collectAsState()
    val errorPesan by viewModel.errorPesan.collectAsState()

    val tinggiValid = tinggi.toFloatOrNull()
    val beratValid = berat.toFloatOrNull()

    val mainGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3))
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.judul_analisis),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.kembali),
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FF))
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(mainGradient)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.instruksi_lengkapi_data),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.sub_instruksi_data),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 20.dp, shape = RoundedCornerShape(32.dp), spotColor = Color(0xFF6200EE)),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(28.dp)) {

                        // INPUT TINGGI
                        InputLabel(stringResource(R.string.label_tinggi))
                        OutlinedTextField(
                            value = tinggi,
                            onValueChange = { viewModel.updateTinggi(it) },
                            placeholder = { Text(stringResource(R.string.contoh_tinggi)) },
                            suffix = { Text(stringResource(R.string.satuan_cm), fontWeight = FontWeight.ExtraBold, color = Color(0xFF6200EE)) },
                            leadingIcon = { Icon(Icons.Default.Straighten, null, tint = Color(0xFF6200EE)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true,
                            isError = errorPesan?.contains("Tinggi") == true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6200EE),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color(0xFFF5F2FF)
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // INPUT BERAT
                        InputLabel(stringResource(R.string.label_berat))
                        OutlinedTextField(
                            value = berat,
                            onValueChange = { viewModel.updateBerat(it) },
                            placeholder = { Text(stringResource(R.string.contoh_berat)) },
                            suffix = { Text(stringResource(R.string.satuan_kg), fontWeight = FontWeight.ExtraBold, color = Color(0xFF03DAC5)) },
                            leadingIcon = { Icon(Icons.Default.MonitorWeight, null, tint = Color(0xFF03DAC5)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true,
                            isError = errorPesan?.contains("Berat") == true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF03DAC5),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color(0xFFF0FAF9)
                            )
                        )
                        // PESAN ERROR (Muncul secara halus jika ada)
                        AnimatedVisibility(visible = errorPesan != null) {
                            Row(
                                modifier = Modifier.padding(top = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = errorPesan ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // TOMBOL HITUNG
                Button(
                    onClick = {
                        // Jalankan validasi di ViewModel
                        if (viewModel.hitungBmi(userId)) {
                            // Jika valid (logis), baru pindah halaman
                            onHitung(tinggiValid!!, beratValid!!)
                        }
                    },
                    // Tombol aktif hanya jika format angka benar (tidak kosong)
                    enabled = tinggiValid != null && beratValid != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.tombol_hitung_bmi).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    color = Color(0xFFE3F2FD),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.tips_presisi),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1565C0)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF333333)
        ),
        modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
    )
}
