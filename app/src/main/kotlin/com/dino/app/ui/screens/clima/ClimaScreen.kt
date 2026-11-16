package com.dino.app.ui.screens.clima

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.PaleoApplication
import com.dino.app.data.DinoData
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle

@Composable
fun ClimaScreen() {
    val prefs = (LocalContext.current.applicationContext as PaleoApplication).prefs
    var periodo by remember { mutableStateOf(DinoData.CLIMA.keys.first()) }
    val dados = DinoData.CLIMA[periodo]!!
    LaunchedEffect(periodo) { prefs.visitarPeriodo(periodo.substringBefore(" ")) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        PaleoCard {
            SectionTitle("Clima Mesozóico", "🌍")
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(periodo) }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DinoData.CLIMA.keys.forEach { op -> DropdownMenuItem(text = { Text(op) }, onClick = { periodo = op; expanded = false }) }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("🌡️ Temperatura: ${dados.temperatura}°C")
            Text("💨 CO₂: ${dados.co2} ppm")
            Text("🌿 Vegetação: ${dados.vegetacao}")
            Text("🌊 Nível do mar: ${dados.nivelMar}")
            Spacer(Modifier.height(8.dp))
            Text(dados.descricao)
        }
    }
}