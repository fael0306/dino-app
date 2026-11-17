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
import com.dino.app.ui.components.ScatterPoint
import com.dino.app.ui.components.SectionTitle
import kotlin.math.abs

@Composable
fun MassaScreen() {
    var circ by remember { mutableStateOf("50") }
    var bipede by remember { mutableStateOf(true) }
    var resultadoTon by remember { mutableStateOf<Double?>(null) }
    var estimativa by remember { mutableStateOf<Pair<Float, Float>?>(null) } // (fêmur em mm, massa em ton) fixados no cálculo
    var invalido by remember { mutableStateOf(false) }

    val pontosFixos = remember {
        DinoData.CLASSICOS.map {
            val bip = it.Postura.lowercase().contains("bípede")
            ScatterPoint(it.Nome, (Simulacoes.circFemoralPorMassa(it.Peso, bip) * 10f).toFloat(), it.Peso.toFloat())
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
            OutlinedTextField(
                value = circ, onValueChange = { circ = it.filter { c -> c.isDigit() || c == '.' }; invalido = false },
                label = { Text("Fêmur (cm)") }, isError = invalido, modifier = Modifier.fillMaxWidth()
            )
            if (invalido) Text("Digite um número maior que zero.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val c = circ.toDoubleOrNull()
                if (c == null || c <= 0.0) { invalido = true; return@Button }
                val ton = Simulacoes.calcularMassaTon(c, bipede)
                resultadoTon = ton
                estimativa = (c * 10).toFloat() to ton.toFloat()
            }, modifier = Modifier.fillMaxWidth()) { Text("Calcular") }
            resultadoTon?.let { ton ->
                Spacer(Modifier.height(12.dp))
                Text("Massa: ${"%.2f".format(ton)} ton (${"%.0f".format(ton * 1000)} kg)", style = MaterialTheme.typography.titleMedium)
                pontosFixos.minByOrNull { abs(it.y - ton.toFloat()) }?.let {
                    Text("Mais próximo em massa: ${it.rotulo} (${"%.2f".format(it.y)} ton)", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        PaleoCard {
            SectionTitle("Comparação", "📊")
            ScatterChart(points = pontosFixos, destaque = estimativa)
        }
    }
}