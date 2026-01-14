package com.example.roomdb_kalkulatorbmi.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomdb_kalkulatorbmi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanUtama(
    namaUser: String,      // DIDAPAT DARI: Database (melalui UtamaViewModel di PetaNavigasi)
    onInputBmi: () -> Unit, // AKSI: Berpindah ke Halaman Entry
    onRiwayat: () -> Unit,  // AKSI: Berpindah ke Halaman Riwayat
    onLogout: () -> Unit    // AKSI: Kembali ke Halaman Home (Ganti User)
) {
    /////// TEMA VISUAL
    // Gradasi Latar Belakang dari biru muda ke putih agar terlihat bersih dan modern
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF0F2FF), Color(0xFFFFFFFF))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.judul_dashboard),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = Color(0xFF6200EE)
                        )
                    )
                },
                actions = {
                    /////// TOMBOL LOGOUT (Ganti User)
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFEBEE),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = stringResource(R.string.cd_keluar),
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                ///////////////////////////////////////////////////////////////////////////
                // 1. WELCOME CARD (Kartu Sapaan Pengguna)
                ///////////////////////////////////////////////////////////////////////////
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF6200EE), Color(0xFF9C27B0))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.CenterStart)) {
                            Text(
                                text = stringResource(R.string.salam_pengguna),
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.titleMedium
                            )
                            /////// DATA NAMA
                            // Nama ini diambil dari Entity User yang sedang aktif
                            Text(
                                text = namaUser,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.sub_salam),
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // Dekorasi Ikon Roket transparan di latar belakang kartu
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 10.dp, y = 10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = stringResource(R.string.pertanyaan_tindakan),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(20.dp))

                ///////////////////////////////////////////////////////////////////////////
                // 2. MENU CARDS (Navigasi ke Fitur Utama)
                ///////////////////////////////////////////////////////////////////////////

                // --- MENU INPUT BMI ---
                MenuCardModern(
                    title = stringResource(R.string.menu_input_bmi),
                    subtitle = stringResource(R.string.sub_menu_input_bmi),
                    icon = Icons.Default.MonitorWeight,
                    mainColor = Color(0xFF6200EE),
                    onClick = onInputBmi
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- MENU RIWAYAT ---
                MenuCardModern(
                    title = stringResource(R.string.menu_riwayat),
                    subtitle = stringResource(R.string.sub_menu_riwayat),
                    icon = Icons.Default.History,
                    mainColor = Color(0xFF03DAC5),
                    onClick = onRiwayat
                )
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// 3. KOMPONEN UI CUSTOM (Menu Card)
///////////////////////////////////////////////////////////////////////////
@Composable
fun MenuCardModern(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    mainColor: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Wadah Ikon dengan latar transparan sesuai warna tema menu
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = mainColor.copy(alpha = 0.15f),
                modifier = Modifier.size(70.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(18.dp).fillMaxSize(),
                    tint = mainColor
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF333333)
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
            }

            // Ikon panah kecil sebagai indikator klik
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp).rotate(180f)
            )
        }
    }
}