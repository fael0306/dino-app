package com.dino.app.ui.screens.icnofosseis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData
import com.dino.app.data.model.Icnofossil
import com.dino.app.domain.Simulacoes
import com.dino.app.paleoApp
import com.dino.app.ui.components.PaleoCard
import com.dino.app.ui.components.SectionTitle

private fun descrever(i: Icnofossil): String = listOfNotNull(
    "${i.dedos} dedos",
    if (i.garras) "com garras" else "sem garras",
    i.tamanho?.let { "tamanho $it" },
    i.forma?.let { "forma $it" },
    i.proporcao?.let { "proporção $it" }
).joinToString(", ")

@Composable
fun IcnofosseisScreen() {
    val app = LocalContext.current.paleoApp
    var desafio by remember { mutableStateOf<String?>(null) }
    var dedos by remember { mutableIntStateOf(3) }
    var garras by remember { mutableStateOf(true) }
    var tamanho by remember { mutableStateOf<String?>(null) }
    var forma by remember { mutableStateOf<String?>(null) }
    var proporcao by remember { mutableStateOf<String?>(null) }
    var resultado by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaleoCard {
            SectionTitle("Paleo-Detetive", "👣")
            Button(onClick = {
                desafio = DinoData.ICNOFOSSEIS.keys.random()
                dedos = 3; garras = true; tamanho = null; forma = null; proporcao = null; resultado = null
            }, modifier = Modifier.fillMaxWidth()) { Text("🎲 Novo Desafio") }
            desafio?.let { alvo ->
                Spacer(Modifier.height(12.dp))
                Text("Fóssil misterioso: ???", style = MaterialTheme.typography.titleMedium)
                DinoData.ICNOFOSSEIS[alvo]?.let { Text("Pegada observada: ${descrever(it)}", style = MaterialTheme.typography.bodyMedium) }
                Spacer(Modifier.height(8.dp))
                Text("1. Dedos:"); Row { FilterChip(selected = dedos == 3, onClick = { dedos = 3 }, label = { Text("3") }); Spacer(Modifier.width(8.dp)); FilterChip(selected = dedos == 4, onClick = { dedos = 4 }, label = { Text("4") }) }
                Spacer(Modifier.height(8.dp))
                Text("2. Garras?"); Row { FilterChip(selected = garras, onClick = { garras = true }, label = { Text("Sim") }); Spacer(Modifier.width(8.dp)); FilterChip(selected = !garras, onClick = { garras = false }, label = { Text("Não") }) }
                if (dedos == 3) {
                    Spacer(Modifier.height(8.dp))
                    Text("3. Tamanho:"); Row { FilterChip(selected = tamanho == "pequeno", onClick = { tamanho = "pequeno" }, label = { Text("Pequeno") }); Spacer(Modifier.width(8.dp)); FilterChip(selected = tamanho == "grande", onClick = { tamanho = "grande" }, label = { Text("Grande") }) }
                    if (garras) {
                        Spacer(Modifier.height(8.dp))
                        Text("4. Forma:"); Row { FilterChip(selected = forma == "alongada", onClick = { forma = "alongada" }, label = { Text("Alongada") }); Spacer(Modifier.width(8.dp)); FilterChip(selected = forma == "larga", onClick = { forma = "larga" }, label = { Text("Larga") }) }
                    }
                } else if (!garras) {
                    Spacer(Modifier.height(8.dp))
                    Text("3. Proporção:"); Row { FilterChip(selected = proporcao == "larga", onClick = { proporcao = "larga" }, label = { Text("Larga") }); Spacer(Modifier.width(8.dp)); FilterChip(selected = proporcao == "alongada", onClick = { proporcao = "alongada" }, label = { Text("Alongada") }) }
                }
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    val r = Simulacoes.identificarIcnogenus(dedos, garras, tamanho, forma, proporcao)
                    resultado = r
                    if (r == alvo) app.achievements.desbloquear("detetive_icno")
                }, modifier = Modifier.fillMaxWidth()) { Text("Identificar") }
                resultado?.let { r ->
                    Spacer(Modifier.height(8.dp))
                    Text(if (r == alvo) "✅ Acertou! É $r" else "❌ Você disse $r, mas era $alvo", color = if (r == alvo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
