package com.dino.app.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData
import com.dino.app.data.model.BatalhaPergunta
import com.dino.app.data.model.QuizQuestion
import com.dino.app.domain.BatalhaGenerator
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle

@Composable
fun QuizScreen() {
    var modo by remember { mutableStateOf("normal") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        SectionTitle("Quiz Paleontológico", "📝")
        Row {
            FilterChip(selected = modo == "normal", onClick = { modo = "normal" }, label = { Text("Normal") })
            Spacer(Modifier.width(8.dp))
            FilterChip(selected = modo == "batalha", onClick = { modo = "batalha" }, label = { Text("⚔️ Batalha") })
        }
        Spacer(Modifier.height(12.dp))
        if (modo == "normal") QuizNormal() else QuizBatalha()
    }
}

@Composable
private fun QuizNormal() {
    var nivel by remember { mutableStateOf("Fácil") }
    var estado by remember { mutableStateOf<EstadoQuiz?>(null) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PaleoCard {
            Row { listOf("Fácil", "Médio", "Difícil").forEach { FilterChip(selected = nivel == it, onClick = { nivel = it }, label = { Text(it) }); Spacer(Modifier.width(6.dp)) } }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { estado = EstadoQuiz(DinoData.QUIZ[nivel] ?: emptyList()) }, modifier = Modifier.fillMaxWidth()) { Text("Iniciar") }
        }
        estado?.let { e ->
            if (e.indice >= e.perguntas.size) {
                PaleoCard {
                    Text("🏁 ${e.acertos}/${e.perguntas.size}", style = MaterialTheme.typography.titleMedium)
                    e.perguntas.forEachIndexed { i, p ->
                        Text("${i+1}. ${p.pergunta}")
                        Text(if (e.respostas.getOrNull(i) == p.resposta) "✅ Correta" else "❌ Correta: ${p.opcoes[p.resposta]}")
                    }
                }
            } else {
                val p = e.perguntas[e.indice]
                PaleoCard {
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

private data class EstadoQuiz(val perguntas: List<QuizQuestion>, val indice: Int = 0, val acertos: Int = 0, val respostas: List<Int> = emptyList(), val selecionada: Int? = null)

@Composable
private fun QuizBatalha() {
    val nomes = DinoData.REAIS.map { it.Nome }
    var d1 by remember { mutableStateOf(nomes[0]) }
    var d2 by remember { mutableStateOf(nomes.getOrElse(1) { nomes[0] }) }
    var perguntas by remember { mutableStateOf<List<BatalhaPergunta>?>(null) }
    var indice by remember { mutableStateOf(0) }
    var acertos by remember { mutableStateOf(0) }
    var respostas by remember { mutableStateOf<List<Int>>(emptyList()) }
    var selecionada by remember { mutableStateOf<Int?>(null) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PaleoCard {
            DropdownNomes(nomes, d1) { d1 = it }; Spacer(Modifier.height(8.dp))
            DropdownNomes(nomes, d2) { d2 = it }; Spacer(Modifier.height(8.dp))
            Button(onClick = {
                if (d1 == d2) return@Button
                perguntas = BatalhaGenerator.gerar(DinoData.REAIS.first { it.Nome == d1 }, DinoData.REAIS.first { it.Nome == d2 }, 5)
                indice = 0; acertos = 0; respostas = emptyList(); selecionada = null
            }, modifier = Modifier.fillMaxWidth()) { Text("⚔️ Batalhar!") }
        }
        perguntas?.let { lista ->
            if (indice >= lista.size) {
                PaleoCard { Text("🏁 Batalha: $acertos/${lista.size}") }
            } else {
                val p = lista[indice]
                PaleoCard {
                    Text(p.pergunta, style = MaterialTheme.typography.titleMedium)
                    p.opcoes.forEachIndexed { i, op ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            RadioButton(selected = selecionada == i, onClick = { selecionada = i })
                            Text(op)
                        }
                    }
                    Button(onClick = {
                        val sel = selecionada ?: return@Button
                        respostas = respostas + sel; if (sel == p.resposta) acertos++
                        indice++; selecionada = null
                    }, modifier = Modifier.fillMaxWidth()) { Text("Responder") }
                }
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