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
    return Layout