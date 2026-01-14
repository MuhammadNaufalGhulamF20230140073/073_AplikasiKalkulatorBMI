package com.example.roomdb_kalkulatorbmi.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.roomdb_kalkulatorbmi.R
import com.example.roomdb_kalkulatorbmi.viewmodel.HasilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHasil(
    viewModel: HasilViewModel,
    userId: Int,
    tinggi: Float,
    berat: Float,
    onBack: () -> Unit,
    onLihatOutfit: (Int) -> Unit
) {
    val bmi by viewModel.nilaiBmi.collectAsState()
    val kategori by viewModel.kategori.collectAsState()
    val kategoriId by viewModel.kategoriId.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    // Hitung BMI lokal untuk parameter fungsi
    val tinggiMeter = tinggi / 100f
    val bmiDihitung = berat / (tinggiMeter * tinggiMeter)


    // 🔥 PERBAIKAN: LaunchedEffect hanya update tampilan (Display)
    LaunchedEffect(userId, tinggi, berat) {
        if (userId > 0 && tinggi > 0f && berat > 0f) {
            viewModel.updateDisplayHanya(bmiDihitung)
        }
    }

    val colorStatus = when (kategoriId) {
        1 -> Color(0xFF0077C2) // Blue - Underweight
        2 -> Color(0xFF2E7D32) // Green - Normal
        3 -> Color(0xFFE65100) // Orange - Overweight
        4 -> Color(0xFFC62828) // Red - Obese
        else -> MaterialTheme.colorScheme.primary
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp),
                shape = RoundedCornerShape(28.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.panduan_bmi_who),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 2.dp)
                    Image(
                        painter = painterResource(id = R.drawable.who),
                        contentDescription = stringResource(R.string.cd_panduan_who),
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.tombol_tutup))
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.judul_hasil),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorStatus,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F4F9))
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Brush.verticalGradient(listOf(colorStatus, Color.Transparent)))
            )

            Card(
                modifier = Modifier
                    .offset(y = (-60).dp)
                    .size(280.dp)
                    .shadow(30.dp, CircleShape, spotColor = colorStatus),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.label_bmi_anda),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
                    )
                    Text(
                        text = String.format("%.1f", bmi),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 80.sp,
                            fontWeight = FontWeight.Black,
                            color = colorStatus
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = null,
                        tint = colorStatus.copy(alpha = 0.3f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier.offset(y = (-30).dp).padding(horizontal = 32.dp),
                color = colorStatus,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = if (kategori.isEmpty()) "MENGHITUNG..." else kategori.uppercase(),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .clickable { showDialog = true }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, null, tint = colorStatus, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.link_lihat_panduan),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline,
                        color = colorStatus,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))


            // 🔥 PERBAIKAN: SIMPAN KE DB HANYA DI TOMBOL INI
            Button(
                onClick = {
                    if (kategoriId != 0) {
                        // Jalankan fungsi simpan permanen
                        viewModel.simpanKeRiwayatPermanen(userId, tinggi, berat, bmiDihitung)
                        // Navigasi
                        onLihatOutfit(kategoriId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(70.dp)
                    .shadow(15.dp, RoundedCornerShape(20.dp), spotColor = colorStatus),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorStatus),
                enabled = kategoriId != 0
            ) {
                Text(
                    text = stringResource(R.string.btn_rekomendasi_outfit),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}