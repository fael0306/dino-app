package com.dino.app.ui.screens.fosseis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData
import com.dino.app.ui.components.DinoSilhueta
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle

@Composable
fun FosseisScreen() {
    var dino by remember { mutableStateOf(DinoData.REAIS.random()) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaleoCard {
            SectionTitle("Museu de Fósseis", "🦴")
            Button(onClick = { dino = DinoData.REAIS.random() }, modifier = Modifier.fillMaxWidth()) { Text("🎲 Sortear Dinossauro") }
            Spacer(Modifier.height(16.dp))
            DinoSilhueta(dino.Nome, dino.Altura, Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Text(dino.Nome, style = MaterialTheme.typography.headlineSmall)
            Text("Período: ${dino.Periodo}")
            Text("Dieta: ${dino.Dieta}")
            Text("Comprimento: ${dino.Comprimento} m")
            Text("Peso: ${dino.Peso} ton")
            Spacer(Modifier.height(8.dp))
            Text("💡 ${dino.Curiosidade}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}