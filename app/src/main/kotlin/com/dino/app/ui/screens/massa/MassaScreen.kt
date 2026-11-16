package com.dino.app.ui.screens.massa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData
import com.dino.app.domain.Simulacoes
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.ScatterChart
import com.dino.app.ui.components.SectionTitle

@Composable
fun MassaScreen() {
    var circ by remember { mutableStateOf("50") }
    var bipede by remember { mutableStateOf(true) }
    var resultadoTon by remember { mutableStateOf<Double?>(null) }
    var resultadoKg by remember { mutableStateOf<Double?>(null) }

    val pontosFixos = remember {
        DinoData.CLASSICOS.map {
            val bip = it.Postura.lowercase().contains("bípede")
            (Simulacoes.circFemoralPorMassa(it.Peso, bip) * 10f).toFloat() to it.Peso.toFloat()
        }
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaleoCard {
            SectionTitle("Estimativa de Massa", "⚖️")
            Row {
                FilterChip(selected = bipede, onClick = { bipede = true }, label = { Text("Bípede") })
                Spacer(Modifier.width(8.dp))
                FilterChip(selected = !bipede, onClick = { bipede = false }, label = { Text("Quadrúpede") })
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = circ, onValueChange = { circ = it.filter { c -> c.isDigit() || c == '.' } }, label = { Text("Fêmur (cm)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val c = circ.toDoubleOrNull() ?: return@Button
                val ton = Simulacoes.calcularMassaTon(c, bipede)
                resultadoTon = ton; resultadoKg = ton * 1000
            }, modifier = Modifier.fillMaxWidth()) { Text("Calcular") }
            resultadoTon?.let { ton ->
                Spacer(Modifier.height(12.dp))
                Text("Massa: ${"%.2f".format(ton)} ton (${"%.0f".format(resultadoKg)} kg)", style = MaterialTheme.typography.titleMedium)
            }
        }
        PaleoCard {
            SectionTitle("Comparação", "📊")
            ScatterChart(points = pontosFixos, destaque = resultadoTon?.let { (circ.toFloat() * 10f) to it.toFloat() })
        }
    }
}