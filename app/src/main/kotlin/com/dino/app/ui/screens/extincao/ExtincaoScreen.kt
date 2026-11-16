package com.dino.app.ui.screens.extincao

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dino.app.domain.Simulacoes
import com.dino.app.ui.components.LineChart
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle

@Composable
fun ExtincaoScreen() {
    var bloqueio by remember { mutableFloatStateOf(15f) }
    var chuva by remember { mutableFloatStateOf(40f) }
    var anos by remember { mutableStateOf("30") }
    var resultado by remember { mutableStateOf<Simulacoes.ExtincaoResult?>(null) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaleoCard {
            SectionTitle("Simulador K-Pg", "☄️")
            Text("Bloqueio solar: ${bloqueio.toInt()}%"); Slider(value = bloqueio, onValueChange = { bloqueio = it }, valueRange = 0f..100f)
            Text("Chuva ácida: ${chuva.toInt()}%"); Slider(value = chuva, onValueChange = { chuva = it }, valueRange = 0f..100f)
            OutlinedTextField(value = anos, onValueChange = { anos = it.filter { c -> c.isDigit() } }, label = { Text("Anos (1-50)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Button(onClick = { resultado = Simulacoes.simularExtincao(bloqueio.toDouble(), chuva.toDouble(), anos.toIntOrNull()?.coerceIn(1, 50) ?: 30) }, modifier = Modifier.fillMaxWidth()) { Text("▶️ Simular") }
        }
        resultado?.let { r ->
            PaleoCard {
                SectionTitle("Resultado", "📈")
                LineChart(
                    series = listOf(r.plantas.map { it.toFloat() }, r.herbivoros.map { it.toFloat() }, r.carnivoros.map { it.toFloat() }),
                    colors = listOf(Color(0xFF27AE60), Color(0xFF2980B9), Color(0xFFE74C3C)),
                    labels = listOf("Plantas", "Herbívoros", "Carnívoros")
                )
            }
        }
    }
}