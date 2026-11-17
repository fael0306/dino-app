package com.dino.app.ui.screens.conquistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.data.Conquistas
import com.dino.app.paleoApp

@Composable
fun ConquistasScreen() {
    val prefs = LocalContext.current.paleoApp.prefs
    val conquistas by prefs.conquistasFlow.collectAsState(initial = emptySet())
    val total = Conquistas.TODAS.size

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("🏆 Conquistas (${conquistas.size}/$total)", style = MaterialTheme.typography.headlineSmall)
        LinearProgressIndicator(progress = { conquistas.size.toFloat() / total }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
        LazyColumn {
            items(Conquistas.TODAS) { c ->
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
