package com.dino.app.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData
import com.dino.app.data.QuizData
import com.dino.app.data.model.BatalhaPergunta
import com.dino.app.data.model.QuizQuestion
import com.dino.app.data.model.ResultadoQuiz
import com.dino.app.domain.BatalhaGenerator
import com.dino.app.paleoApp
import com.dino.app.ui.components.LineChart
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuizScreen() {
    var modo by remember { mutableStateOf("normal") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        SectionTitle("Quiz Paleontológico", "📝")
        Row {
            FilterChip(selected = modo == "normal", onClick = { modo = "normal" }, label = { Text("Normal") })
            Spacer(Modifier.width(8.dp))
            FilterChip(selected = modo == "batalha", onClick = { modo = "batalha" }, label = { Text("⚔️ Batalha") })
            Spacer(Modifier.width(8.dp))
            FilterChip(selected = modo == "stats", onClick = { modo = "stats" }, label = { Text("📊 Histórico") })
        }
        Spacer(Modifier.height(12.dp))
        when (modo) {
            "normal" -> QuizNormal()
            "batalha" -> QuizBatalha()
            else -> Estatisticas()
        }
    }
}

private val CONQUISTA_POR_NIVEL = mapOf("Fácil" to "quiz_facil", "Médio" to "quiz_medio", "Difícil" to "quiz_dificil")

private data class EstadoQuiz(
    val nivel: String, val perguntas: List<QuizQuestion>,
    val indice: Int = 0, val acertos: Int = 0, val respostas: List<Int> = emptyList(), val selecionada: Int? = null
)

@Composable
private fun QuizNormal() {
    val app = LocalContext.current.paleoApp
    var nivel by remember { mutableStateOf("Fácil") }
    var estado by remember { mutableStateOf<EstadoQuiz?>(null) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PaleoCard {
            Row { listOf("Fácil", "Médio", "Difícil").forEach { FilterChip(selected = nivel == it, onClick = { nivel = it }, label = { Text(it) }); Spacer(Modifier.width(6.dp)) } }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { estado = EstadoQuiz(nivel, (QuizData.PERGUNTAS[nivel] ?: emptyList()).shuffled()) }, modifier = Modifier.fillMaxWidth()) { Text("Iniciar") }
        }
        estado?.let { e ->
            if (e.indice >= e.perguntas.size) {
                // Fim: registra no histórico e libera a conquista do nível com ≥ 70% de acertos.
                LaunchedEffect(e) {
                    if (e.perguntas.isNotEmpty()) {
                        app.prefs.registrarQuiz(ResultadoQuiz(System.currentTimeMillis(), e.nivel, e.acertos, e.perguntas.size))
                        if (e.acertos * 100 >= 70 * e.perguntas.size) CONQUISTA_POR_NIVEL[e.nivel]?.let { app.achievements.desbloquear(it) }
                    }
                }
                PaleoCard {
                    Text("🏁 ${e.acertos}/${e.perguntas.size}", style = MaterialTheme.typography.titleMedium)
                    e.perguntas.forEachIndexed { i, p ->
                        Spacer(Modifier.height(6.dp))
                        Text("${i + 1}. ${p.pergunta}")
                        Text(if (e.respostas.getOrNull(i) == p.resposta) "✅ Correta" else "❌ Correta: ${p.opcoes[p.resposta]}")
                        if (p.explicacao.isNotBlank()) Text(p.explicacao, style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                val p = e.perguntas[e.indice]
                PaleoCard {
                    Text("Pergunta ${e.indice + 1}/${e.perguntas.size}", style = MaterialTheme.typography.labelMedium)
                    Text(p.pergunta, style = MaterialTheme.typography.titleMedium)
                    p.opcoes.forEachIndexed { i, op ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            RadioButton(selected = e.selecionada == i, onClick = { estado = e.copy(selecionada = i) })
                            Text(op)
                        }
                    }
                    Button(onClick = {
                        val sel = e.selecionada ?: return@Button
                        estado = e.copy(respostas = e.respostas + sel, acertos = if (sel == p.resposta) e.acertos + 1 else e.acertos, indice = e.indice + 1, selecionada = null)
                    }, modifier = Modifier.fillMaxWidth()) { Text("Responder") }
                }
            }
        }
    }
}

@Composable
private fun QuizBatalha() {
    val app = LocalContext.current.paleoApp
    val escopo = rememberCoroutineScope()
    val nomes = DinoData.REAIS.map { it.Nome }
    var d1 by remember { mutableStateOf(nomes[0]) }
    var d2 by remember { mutableStateOf(nomes.getOrElse(1) { nomes[0] }) }
    var perguntas by remember { mutableStateOf<List<BatalhaPergunta>?>(null) }
    var indice by remember { mutableIntStateOf(0) }
    var acertos by remember { mutableIntStateOf(0) }
    var selecionada by remember { mutableStateOf<Int?>(null) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PaleoCard {
            DropdownNomes(nomes, d1) { d1 = it }; Spacer(Modifier.height(8.dp))
            DropdownNomes(nomes, d2) { d2 = it }; Spacer(Modifier.height(8.dp))
            if (d1 == d2) Text("Escolha dois dinossauros diferentes.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            Button(onClick = {
                if (d1 == d2) return@Button
                perguntas = BatalhaGenerator.gerar(DinoData.REAIS.first { it.Nome == d1 }, DinoData.REAIS.first { it.Nome == d2 }, 5)
                indice = 0; acertos = 0; selecionada = null
            }, modifier = Modifier.fillMaxWidth()) { Text("⚔️ Batalhar!") }
        }
        perguntas?.let { lista ->
            if (indice >= lista.size) {
                PaleoCard { Text("🏁 Batalha: $acertos/${lista.size}") }
            } else {
                val p = lista[indice]
                PaleoCard {
                    Text("Pergunta ${indice + 1}/${lista.size}", style = MaterialTheme.typography.labelMedium)
                    Text(p.pergunta, style = MaterialTheme.typography.titleMedium)
                    p.opcoes.forEachIndexed { i, op ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            RadioButton(selected = selecionada == i, onClick = { selecionada = i })
                            Text(op)
                        }
                    }
                    Button(onClick = {
                        val sel = selecionada ?: return@Button
                        if (sel == p.resposta) acertos++
                        indice++; selecionada = null
                        if (indice >= lista.size && lista.isNotEmpty()) {
                            val total = lista.size; val certas = acertos
                            escopo.launch { app.prefs.registrarQuiz(ResultadoQuiz(System.currentTimeMillis(), "Batalha", certas, total)) }
                            if (certas == total) app.achievements.desbloquear("batalha_mestre")
                        }
                    }, modifier = Modifier.fillMaxWidth()) { Text("Responder") }
                }
            }
        }
    }
}

@Composable
private fun Estatisticas() {
    val app = LocalContext.current.paleoApp
    val historico by app.prefs.historicoFlow.collectAsState(initial = emptyList())
    val fmt = remember { SimpleDateFormat("dd/MM HH:mm", Locale("pt", "BR")) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PaleoCard {
            SectionTitle("Histórico", "📊")
            if (historico.isEmpty()) {
                Text("Nenhuma partida registrada ainda.")
            } else {
                Text("Partidas: ${historico.size} · média: ${"%.0f".format(historico.map { it.percentual }.average())}%")
                if (historico.size >= 2) {
                    Spacer(Modifier.height(8.dp))
                    LineChart(listOf(historico.takeLast(20).map { it.percentual }), listOf(Color(0xFF27AE60)), listOf("% de acertos"))
                }
                Spacer(Modifier.height(8.dp))
                historico.takeLast(5).reversed().forEach { Text("${fmt.format(Date(it.timestamp))} · ${it.modo}: ${it.acertos}/${it.total}", style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
}

@Composable
private fun DropdownNomes(nomes: List<String>, selecionado: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(selecionado) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            nomes.forEach { n -> DropdownMenuItem(text = { Text(n) }, onClick = { onSelect(n); expanded = false }) }
        }
    }
}
