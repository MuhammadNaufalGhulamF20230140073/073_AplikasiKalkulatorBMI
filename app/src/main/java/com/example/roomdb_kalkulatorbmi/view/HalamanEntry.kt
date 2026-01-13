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
