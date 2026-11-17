package com.dino.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(series: List<List<Float>>, colors: List<Color>, labels: List<String>, modifier: Modifier = Modifier) {
    val maxY = series.flatten().maxOrNull()?.coerceAtLeast(1f) ?: 1f
    Column(modifier) {
        Canvas(Modifier.fillMaxWidth().height(220.dp)) {
            val w = size.width; val h = size.height
            series.forEachIndexed { si, s ->
                if (s.isEmpty()) return@forEachIndexed
                val path = Path()
                s.forEachIndexed { i, v ->
                    val x = i.toFloat() / (s.size - 1).coerceAtLeast(1) * w
                    val y = h - (v / maxY) * h
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(path, colors[si], style = Stroke(width = 3f))
            }
            drawLine(Color.Gray, Offset(0f, h), Offset(w, h), strokeWidth = 1f)
        }
        Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            labels.forEachIndexed { i, l ->
                Row {
                    Box(Modifier.size(10.dp).background(colors[i], CircleShape))
                    Spacer(Modifier.width(4.dp))
                    Text(l, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun BarChartHorizontal(items: List<Pair<String, Float>>, colors: List<Color>, modifier: Modifier = Modifier) {
    val maxV = items.maxOfOrNull { it.second }?.coerceAtLeast(1f) ?: 1f
    Column(modifier) {
        items.forEachIndexed { i, (label, v) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                Text(label, Modifier.width(90.dp), style = MaterialTheme.typography.labelSmall)
                Box(Modifier.weight(1f).height(20.dp).background(MaterialTheme.colorScheme.surfaceVariant)) {
                    Box(Modifier.fillMaxHeight().fillMaxWidth(v / maxV).background(colors.getOrElse(i) { Color.Gray }))
                }
                Spacer(Modifier.width(8.dp))
                Text("${v.toInt()}", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

data class ScatterPoint(val rotulo: String, val x: Float, val y: Float)

private const val PAD = 16f

private fun projetar(x: Float, y: Float, maxX: Float, maxY: Float, w: Float, h: Float) =
    Offset(PAD + x / maxX * (w - 2 * PAD), h - PAD - y / maxY * (h - 2 * PAD))

/** Gráfico de dispersão; toque em um ponto para ver o rótulo e os valores. */
@Composable
fun ScatterChart(
    points: List<ScatterPoint>,
    destaque: Pair<Float, Float>?,
    modifier: Modifier = Modifier,
    unidadeX: String = "mm",
    unidadeY: String = "ton"
) {
    val maxX = (points.maxOfOrNull { it.x } ?: 1f).coerceAtLeast(destaque?.first ?: 1f).coerceAtLeast(1f)
    val maxY = (points.maxOfOrNull { it.y } ?: 1f).coerceAtLeast(destaque?.second ?: 1f).coerceAtLeast(1f)
    var selecionado by remember(points) { mutableStateOf<ScatterPoint?>(null) }
    val corPonto = MaterialTheme.colorScheme.primary

    Column(modifier) {
        Canvas(
            Modifier.fillMaxWidth().height(250.dp).pointerInput(points, maxX, maxY) {
                detectTapGestures { toque ->
                    val w = size.width.toFloat(); val h = size.height.toFloat()
                    val mais = points.minByOrNull { (projetar(it.x, it.y, maxX, maxY, w, h) - toque).getDistance() }
                    selecionado = mais?.takeIf { (projetar(it.x, it.y, maxX, maxY, w, h) - toque).getDistance() < 56f }
                }
            }
        ) {
            val w = size.width; val h = size.height
            drawLine(Color.Gray, Offset(0f, h), Offset(w, h), strokeWidth = 1f)
            drawLine(Color.Gray, Offset(0f, 0f), Offset(0f, h), strokeWidth = 1f)
            points.forEach {
                drawCircle(Color(0xFF3691E3), radius = 8f, center = projetar(it.x, it.y, maxX, maxY, w, h))
            }
            destaque?.let {
                drawCircle(Color(0xFFFF6384), radius = 12f, center = projetar(it.first, it.second, maxX, maxY, w, h))
            }
            selecionado?.let {
                drawCircle(corPonto, radius = 16f, center = projetar(it.x, it.y, maxX, maxY, w, h), style = Stroke(width = 4f))
            }
        }
        Text(
            selecionado?.let { "${it.rotulo}: fêmur ≈ ${it.x.toInt()} $unidadeX · massa ${"%.2f".format(it.y)} $unidadeY" }
                ?: "Toque em um ponto azul para identificar o dinossauro (o ponto rosa é a sua estimativa).",
            style = MaterialTheme.typography.labelMedium
        )
    }
}