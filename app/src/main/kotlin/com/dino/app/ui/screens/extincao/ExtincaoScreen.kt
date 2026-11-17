package com.dino.app.ui.screens.extincao

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.domain.Simulacoes
import com.dino.app.ui.components.LineChart
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle
import com.dino.app.util.compartilharArquivo
import com.dino.app.util.pastaExportacao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private data class Cenario(val nome: String, val bloqueio: Float, val chuva: Float, val anos: Int)

// Valores ilustrativos para o modelo simplificado, não estimativas geofísicas.
private val CENARIOS = listOf(
    Cenario("☄️ Chicxulub", 85f, 70f, 10),
    Cenario("🌋 Decão", 35f, 60f, 50),
    Cenario("🌱 Recuperação", 5f, 10f, 50)
)

@Composable
fun ExtincaoScreen() {
    val ctx = LocalContext.current
    val escopo = rememberCoroutineScope()
    var bloqueio by remember { mutableFloatStateOf(15f) }
    var chuva by remember { mutableFloatStateOf(40f) }
    var anos by remember { mutableStateOf("30") }
    var resultado by remember { mutableStateOf<Simulacoes.ExtincaoResult?>(null) }
    var status by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaleoCard {
            SectionTitle("Simulador K-Pg", "☄️")
            Text("Cenários (valores ilustrativos)", style = MaterialTheme.typography.labelLarge)
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CENARIOS.forEach { c ->
                    AssistChip(
                        onClick = { bloqueio = c.bloqueio; chuva = c.chuva; anos = c.anos.toString(); resultado = null; status = null },
                        label = { Text(c.nome) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
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
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = {
                    escopo.launch {
                        val arquivo = withContext(Dispatchers.IO) {
                            File(ctx.pastaExportacao(), "simulacao_kpg_${System.currentTimeMillis()}.csv").also { it.writeText(Simulacoes.paraCsv(r)) }
                        }
                        status = if (ctx.compartilharArquivo(arquivo, "text/csv", "Exportar simulação")) null
                        else "Não foi possível compartilhar o arquivo."
                    }
                }, modifier = Modifier.fillMaxWidth()) { Text("📤 Exportar CSV") }
                status?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
}