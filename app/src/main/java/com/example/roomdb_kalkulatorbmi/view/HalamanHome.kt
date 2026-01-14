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

                ///////////////////////////////////////////////////////////////////////////
                // 1. HEADER SECTION (Salam Selamat Datang)
                ///////////////////////////////////////////////////////////////////////////
                item {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            text = stringResource(R.string.salam_selamat_datang),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = stringResource(R.string.instruksi_awal),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                ///////////////////////////////////////////////////////////////////////////
                // 2. INPUT SECTION (Tambah User Baru)
                ///////////////////////////////////////////////////////////////////////////
                item {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            OutlinedTextField(
                                value = inputNama,
                                onValueChange = { inputNama = it },
                                placeholder = { Text(stringResource(R.string.petunjuk_nama)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFF6200EE)) },
                                singleLine = true
                            )

                            Spacer(Modifier.height(16.dp))

                            /////// AKSI: TOMBOL DAFTAR
                            // Mengirim data nama ke HomeViewModel untuk disimpan ke Room Database
                            Button(
                                onClick = {
                                    if (inputNama.isNotBlank()) {
                                        viewModel.tambahUser(inputNama)
                                        inputNama = "" // Reset kolom setelah klik
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                            ) {
                                Icon(Icons.Default.Add, null)
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.tombol_daftar), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.judul_daftar_profil),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                ///////////////////////////////////////////////////////////////////////////
                // 3. LIST USER SECTION (Daftar Profil dari Database)
                ///////////////////////////////////////////////////////////////////////////

                /////// KONDISI: JIKA DATA KOSONG
                if (daftarUser.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.AccountCircle, null, Modifier.size(64.dp), Color.LightGray)
                            Text(stringResource(R.string.pesan_kosong), color = Color.Gray)
                        }
                    }
                }

                /////// KONDISI: MENAMPILKAN ITEM USER
                items(daftarUser) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar Bulat (Huruf Inisial Nama)
                            Surface(
                                modifier = Modifier.size(54.dp),
                                shape = CircleShape,
                                color = Color(0xFF6200EE).copy(alpha = 0.1f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = user.nama.take(1).uppercase(),
                                        color = Color(0xFF6200EE),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp
                                    )
                                }
                            }

                            Spacer(Modifier.width(16.dp))

                            /////// INFO KOLOM (Nama & Metadata Tanggal)
                            // Data ini didapat dari UserEntity di Room
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = user.nama,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Menampilkan Tanggal Dibuat (Data dari DB)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarToday, null, Modifier.size(10.dp), Color.Gray)
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = "Joined: ${user.tanggalDibuat}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray.copy(alpha = 0.7f),
                                        fontSize = 10.sp
                                    )
                                }

                                // Menampilkan Tanggal Diubah (Update otomatis saat hitung BMI)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Update, null, Modifier.size(10.dp), Color(0xFF4CAF50))
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = "Active: ${user.tanggalDiubah}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF4CAF50),
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            /////// ACTION BUTTONS (Lanjut & Hapus)
                            Row {
                                // Tombol Play: Lanjut ke kalkulator BMI untuk user ini
                                FilledIconButton(
                                    onClick = { onLanjut(user) },
                                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF6200EE)),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }

                                Spacer(Modifier.width(8.dp))

                                // Tombol Sampah: Menghapus user melalui HomeViewModel -> Repository
                                IconButton(
                                    onClick = { viewModel.hapusUser(user) },
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                ///////////////////////////////////////////////////////////////////////////
                // 4. FOOTER SECTION (Display Gambar Animasi/GIF)
                ///////////////////////////////////////////////////////////////////////////
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    AsyncImage(
                        model = R.drawable.halamahome, // DIDAPAT DARI: res/drawable
                        contentDescription = null,
                        imageLoader = pemutarGambar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
            }
        }
    }
}