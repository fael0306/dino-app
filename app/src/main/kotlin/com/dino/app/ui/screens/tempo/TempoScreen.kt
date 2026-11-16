package com.dino.app.ui.screens.tempo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData
import com.dino.app.ui.components.BarChartHorizontal
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle

@Composable
fun TempoScreen() {
    var idade by remember { mutableFloatStateOf(150f) }
    val periodo = DinoData.PERIODOS.firstOrNull { idade <= it.inicio && idade >= it.fim } ?: DinoData.PERIODOS[0]
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaleoCard {
            SectionTitle("Linha do Tempo", "⏳")
            Text("${idade.toInt()} Ma"); Slider(value = idade, onValueChange = { idade = it }, valueRange = 66f..252f)
            Text(periodo.nome, style = MaterialTheme.typography.headlineSmall)
            Text("Eventos: ${periodo.eventos.joinToString()}")
        }
        PaleoCard {
            SectionTitle("Duração", "📊")
            BarChartHorizontal(items = DinoData.PERIODOS.map { it.nome to (it.inicio - it.fim).toFloat() }, colors = DinoData.PERIODOS.map { Color(it.cor) })
        }
    }
}