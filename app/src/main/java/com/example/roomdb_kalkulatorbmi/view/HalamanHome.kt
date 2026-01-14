package com.example.roomdb_kalkulatorbmi.view

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.roomdb_kalkulatorbmi.R
import com.example.roomdb_kalkulatorbmi.room.entity.UserEntity
import com.example.roomdb_kalkulatorbmi.viewmodel.HomeViewModel
import com.example.roomdb_kalkulatorbmi.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    onLanjut: (UserEntity) -> Unit,
    // [viewModel] -> Didapat dari PenyediaViewModel.Factory
    // Menghubungkan layar ini dengan logika bisnis dan database.
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    /////// STATE OBSERVATION
    // Mengambil daftar user dari database secara real-time (Flow -> State)
    val daftarUser by viewModel.userList.collectAsState()

    // State lokal untuk menampung input teks sementara di TextField
    var inputNama by remember { mutableStateOf("") }

    val konteks = LocalContext.current

    /////// KONFIGURASI GIF (COIL)
    // Menyiapkan pemutar gambar agar bisa menampilkan file .gif di bagian bawah
    val pemutarGambar = remember {
        ImageLoader.Builder(konteks)
            .components {
                if (SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }.build()
    }

    // Dekorasi Gradasi Background
    val gradasiLatar = Brush.verticalGradient(
        colors = listOf(Color(0xFFF8F9FF), Color(0xFFECEEFB))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.judul_utama),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                color = Color(0xFF6200EE)
                            )
                        )
                        Text(
                            text = stringResource(R.string.sub_judul_utama),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { spasiDalam ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradasiLatar)
                .padding(spasiDalam)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {