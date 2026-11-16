package com.dino.app.ui.screens.conquistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.PaleoApplication

data class ConquistaInfo(val id: String, val nome: String, val emoji: String)
private val TODAS = listOf(
    ConquistaInfo("quiz_facil", "Quiz Fácil", "🥇"), ConquistaInfo("quiz_medio", "Quiz Médio", "🥈"),
    ConquistaInfo("quiz_dificil", "Quiz Difícil", "🥉"), ConquistaInfo("explorador_escala", "Explorador", "🔍"),
    ConquistaInfo("detetive_icno", "Detetive", "🕵️"), ConquistaInfo("climaturista", "Climaturista", "🌤️"),
    ConquistaInfo("batalha_mestre", "Batalha Mestre", "⚔️")
)

@Composable
fun ConquistasScreen() {
    val prefs = (LocalContext.current.applicationContext as PaleoApplication).prefs
    val conquistas by prefs.conquistasFlow.collectAsState(initial = emptySet())

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("🏆 Conquistas (${conquistas.size}/${TODAS.size})", style = MaterialTheme.typography.headlineSmall)
        LinearProgressIndicator(progress = { conquistas.size.toFloat() / TODAS.size }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
        LazyColumn {
            items(TODAS) { c ->
                val ok = c.id in conquistas
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(12.dp)) {
                        Text(c.emoji, style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(c.nome, style = MaterialTheme.typography.titleMedium)
                            Text(if (ok) "✅ Desbloqueada" else "🔒 Bloqueada", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}