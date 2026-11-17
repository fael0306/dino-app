package com.dino.app.ui.screens.arvore

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.dino.app.data.ArvoreInfo
import com.dino.app.data.DinoData

private class LayoutArvore(val posicoes: Map<String, Offset>, val arestas: List<Pair<String, String>>)

private fun calcularLayout(arestas: List<Pair<String, String>>): LayoutArvore {
    val niveis = mutableMapOf<String, Int>()
    val raizes = arestas.map { it.first }.toSet() - arestas.map { it.second }.toSet()
    val fila = ArrayDeque(raizes.map { it to 0 })
    while (fila.isNotEmpty()) {
        val (n, niv) = fila.removeFirst(); niveis[n] = niv
        arestas.filter { it.first == n }.forEach { fila.addLast(it.second to niv + 1) }
    }
    val porNivel = niveis.entries.groupBy({ it.value }, { it.key })
    val pos = mutableMapOf<String, Offset>()
    porNivel.forEach { (niv, nos) -> nos.forEachIndexed { idx, nome -> pos[nome] = Offset(idx * 260f + 40f, niv * 110f + 60f) } }
    return LayoutArvore(pos, arestas)
}

@Composable
fun ArvoreScreen() {
    val layout = remember { calcularLayout(DinoData.ARVORE_ARESTAS) }
    var escala by remember { mutableFloatStateOf(1f) }
    var deslocamento by remember { mutableStateOf(Offset(20f, 20f)) }
    var selecionado by remember { mutableStateOf<String?>(null) }
    val pincel = remember { Paint().apply { textSize = 30f; isAntiAlias = true } }
    val corLinha = MaterialTheme.colorScheme.onSurfaceVariant
    val corNo = MaterialTheme.colorScheme.primary
    val corSel = MaterialTheme.colorScheme.tertiary
    val corTexto = MaterialTheme.colorScheme.onBackground

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Arvore Evolutiva", style = MaterialTheme.typography.headlineSmall)
        Text("Arraste, use pinca para ampliar e toque em um grupo.", style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(8.dp))
        Canvas(
            Modifier.fillMaxSize().clipToBounds()
                .pointerInput(Unit) {
                    detectTransformGestures { centroide, pan, zoom, _ ->
                        val nova = (escala * zoom).coerceIn(0.4f, 3f)
                        val f = nova / escala
                        deslocamento = (deslocamento - centroide) * f + centroide + pan
                        escala = nova
                    }
                }
                .pointerInput(layout) {
                    detectTapGestures { toque ->
                        val mundo = (toque - deslocamento) / escala
                        val mais = layout.posicoes.minByOrNull { (it.value - mundo).getDistance() }
                        selecionado = mais?.takeIf { (it.value - mundo).getDistance() < 60f }?.key
                    }
                }
        ) {
            withTransform({
                translate(deslocamento.x, deslocamento.y)
                scale(escala, escala, pivot = Offset.Zero)
            }) {
                layout.arestas.forEach { (a, b) ->
                    val pa = layout.posicoes[a]; val pb = layout.posicoes[b]
                    if (pa != null && pb != null) drawLine(corLinha, pa, pb, strokeWidth = 2f)
                }
                pincel.color = corTexto.toArgb()
                layout.posicoes.forEach { (nome, p) ->
                    drawCircle(if (nome == selecionado) corSel else corNo, radius = if (nome == selecionado) 14f else 8f, center = p)
                    drawContext.canvas.nativeCanvas.drawText(nome, p.x + 16f, p.y + 10f, pincel)
                }
            }
        }
    }

    selecionado?.let { nome ->
        AlertDialog(
            onDismissRequest = { selecionado = null },
            confirmButton = { TextButton(onClick = { selecionado = null }) { Text("Fechar") } },
            title = { Text(nome) },
            text = { Text(ArvoreInfo.DESCRICOES[nome] ?: "Sem descricao disponivel.") }
        )
    }
}
