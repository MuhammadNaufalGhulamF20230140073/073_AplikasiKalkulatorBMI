package com.example.roomdb_kalkulatorbmi.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomdb_kalkulatorbmi.R
import com.example.roomdb_kalkulatorbmi.viewmodel.OutfitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanOutfit(
    kategoriId: Int, // DIDAPAT DARI: Hasil hitungan BMI di layar sebelumnya
    onBack: () -> Unit,
    onSelesai: () -> Unit,
    viewModel: OutfitViewModel // DIDAPAT DARI: PenyediaViewModel.Factory
) {
    /////// STATE OBSERVATION
    // Mengamati daftar outfit yang diambil dari database melalui ViewModel
    val outfitList by viewModel.outfitList.collectAsState()
    val listState = rememberLazyListState()

    /////// TRIGGER DATA LOAD
    // LaunchedEffect akan berjalan otomatis saat kategoriId diterima.
    // Ini memerintahkan ViewModel mencari baju yang cocok di database.
    LaunchedEffect(kategoriId) {
        viewModel.loadOutfit(kategoriId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.judul_outfit),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            color = Color.DarkGray
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape)
                            .shadow(2.dp, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF8F9FA), Color(0xFFE9ECEF))
                    )
                )
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                ///////////////////////////////////////////////////////////////////////////
                // 1. HEADER SECTION (Judul & Kurasi Gaya)
                ///////////////////////////////////////////////////////////////////////////
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Text(
                        text = stringResource(R.string.label_kurasi).uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 3.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.sub_judul_outfit),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp
                        ),
                        color = Color(0xFF212529)
                    )
                }

                ///////////////////////////////////////////////////////////////////////////
                // 2. CATALOGUE SECTION (Horizontal List dari Database)
                ///////////////////////////////////////////////////////////////////////////
                if (outfitList.isEmpty()) {
                    // Tampilan Loading jika database masih memproses data
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    // LazyRow digunakan untuk menggeser katalog baju ke samping (Kanan-Kiri)
                    LazyRow(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(28.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(outfitList) { outfit ->
                            Card(
                                modifier = Modifier
                                    .width(320.dp)
                                    .fillMaxHeight()
                                    .shadow(
                                        elevation = 20.dp,
                                        shape = RoundedCornerShape(40.dp),
                                        spotColor = Color.Black.copy(alpha = 0.5f)
                                    ),
                                shape = RoundedCornerShape(40.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {

                                    /////// GAMBAR OUTFIT
                                    // Mengambil ID gambar (R.drawable.xxx) dari data Master di database
                                    Box(
                                        modifier = Modifier.fillMaxWidth().weight(1.4f)
                                    ) {
                                        Image(
                                            painter = painterResource(outfit.gambarResId),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Box(
                                            modifier = Modifier.fillMaxSize().background(
                                                Brush.verticalGradient(
                                                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f))
                                                )
                                            )
                                        )
                                    }

                                    /////// DESKRIPSI TIPS GAYA
                                    // Teks ini disesuaikan secara otomatis berdasarkan kategori BMI user
                                    Column(
                                        modifier = Modifier.weight(0.6f).padding(24.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier.size(8.dp, 24.dp)
                                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                                            )
                                            Spacer(Modifier.width(12.dp))
                                            Text(
                                                text = stringResource(R.string.label_tips_gaya),
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                            text = outfit.deskripsi,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                lineHeight = 24.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 18.sp
                                            ),
                                            color = Color(0xFF343A40)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ///////////////////////////////////////////////////////////////////////////
                // 3. FOOTER SECTION (Tombol Selesai)
                ///////////////////////////////////////////////////////////////////////////
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    shadowElevation = 25.dp
                ) {
                    Button(
                        onClick = onSelesai, // AKSI: Kembali ke layar utama/daftar user
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .height(68.dp)
                            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212529))
                    ) {
                        Text(
                            text = stringResource(R.string.tombol_selesai).uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        )
                    }
                }
            }
        }
    }
}