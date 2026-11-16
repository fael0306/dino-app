package com.dino.app.ui.screens.arvore

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.dino.app.data.DinoData

@Composable
fun ArvoreScreen() {
    val arestas = DinoData.ARVORE_ARESTAS
    val niveis = mutableMapOf<String, Int>()
    val roots = arestas.map { it.first }.toSet() - arestas.map { it.second }.toSet()
    val fila = ArrayDeque(roots.map { it to 0 })
    while (fila.isNotEmpty()) {
        val (n, niv) = fila.removeFirst(); niveis[n] = niv
        arestas.filter { it.first == n }.forEach { fila.addLast(it.second to niv + 1) }
    }
    val nosPorNivel = niveis.entries.groupBy { it.value }.mapValues { it.value.map { e -> e.key } }
    val largura = (nosPorNivel.values.maxOfOrNull { it.size } ?: 1) * 140 + 40
    val altura = (nosPorNivel.size) * 100 + 40

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("🌳 Árvore Evolutiva", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxSize().horizontalScroll(rememberScrollState()).verticalScroll(rememberScrollState())) {
            Canvas(Modifier.width(largura.dp).height(altura.dp)) {
                val pos = mutableMapOf<String, Offset>()
                nosPorNivel.forEach { (niv, nos) ->
                    nos.forEachIndexed { idx, nome -> pos[nome] = Offset((idx + 1f) * 140f, (niv + 1f) * 100f) }
                }
                arestas.forEach { (a, b) -> pos[a]?.let { pa -> pos[b]?.let { pb -> drawLine(Color.Gray, pa, pb, strokeWidth = 2f) } } }
                pos.forEach { (nome, p) ->
                    drawCircle(Color(0xFF2C3E50), radius = 8f, center = p)
                    drawContext.canvas.nativeCanvas.drawText(nome, p.x + 12f, p.y + 4f, Paint().apply { color = android.graphics.Color.DKGRAY; textSize = 30f })
                }
            }
        }
    }
}