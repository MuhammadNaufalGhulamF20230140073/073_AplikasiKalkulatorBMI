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