package com.dino.app.ui.screens.escala

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData
import com.dino.app.paleoApp
import com.dino.app.ui.components.DinoSilhueta
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle

@Composable
fun EscalaScreen() {
    val app = LocalContext.current.paleoApp
    val classicos = DinoData.CLASSICOS
    var dino by remember { mutableStateOf(classicos[0]) }
    var vistos by remember { mutableStateOf(setOf(classicos[0].Nome)) }
    var ref1 by remember { mutableStateOf("Humano") }
    var ref2 by remember { mutableStateOf("Nenhum") }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaleoCard {
            SectionTitle("Compare a Escala", "📏")
            Text("Dinossauro", style = MaterialTheme.typography.labelLarge)
            DropdownGenerico(classicos.map { it.Nome }, dino.Nome) { nome ->
                classicos.firstOrNull { it.Nome == nome }?.let {
                    dino = it
                    vistos = vistos + it.Nome
                    if (vistos.size >= 3) app.achievements.desbloquear("explorador_escala")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Comparar com", style = MaterialTheme.typography.labelLarge)
            DropdownGenerico(listOf("Humano", "Elefante", "Outro"), ref1) { ref1 = it }
            Spacer(Modifier.height(8.dp))
            Text("Comparar também", style = MaterialTheme.typography.labelLarge)
            DropdownGenerico(listOf("Nenhum", "Humano", "Elefante", "Outro"), ref2) { ref2 = it }
        }
        PaleoCard {
            SectionTitle("Visualização", "🦴")
            Row(Modifier.fillMaxWidth().height(280.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EscalaItem(ref1, alturaRef(ref1), Modifier.weight(1f))
                EscalaItem(dino.Nome, dino.Altura, Modifier.weight(1f))
                if (ref2 != "Nenhum") EscalaItem(ref2, alturaRef(ref2), Modifier.weight(1f))
            }
        }
    }
}

private fun alturaRef(nome: String): Double = when (nome) {
    "Humano" -> 1.7; "Elefante" -> 3.3
    else -> DinoData.CLASSICOS.firstOrNull { it.Nome == nome }?.Altura ?: 1.7
}

@Composable
private fun EscalaItem(nome: String, altura: Double, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        DinoSilhueta(nome, altura, Modifier.fillMaxWidth())
        Text(nome, style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}

@Composable
private fun DropdownGenerico(opcoes: List<String>, selecionado: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(selecionado) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opcoes.forEach { op -> DropdownMenuItem(text = { Text(op) }, onClick = { onSelect(op); expanded = false }) }
        }
    }
}
