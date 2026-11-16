package com.dino.app.ui.screens.pdf

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.PaleoApplication
import com.dino.app.data.DinoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun PdfScreen() {
    val ctx = LocalContext.current
    val prefs = (ctx.applicationContext as PaleoApplication).prefs
    val escopo = rememberCoroutineScope()
    var status by remember { mutableStateOf<String?>(null) }
    val conquistas by prefs.conquistasFlow.collectAsState(initial = emptySet())

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("📄 Exportar Relatório", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            escopo.launch {
                status = "Gerando..."
                val file = withContext(Dispatchers.IO) { gerarPdf(ctx, conquistas) }
                status = "✅ Salvo em: ${file.absolutePath}"
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Gerar PDF") }
        status?.let { Text(it, style = MaterialTheme.typography.bodyMedium); Spacer(Modifier.height(12.dp)) }
    }
}

private fun gerarPdf(ctx: Context, conquistas: Set<String>): File {
    val doc = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = doc.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint().apply { textSize = 14f }
    var y = 40f

    paint.textSize = 22f; paint.isFakeBoldText = true
    canvas.drawText("PaleoLab — Relatório Científico", 40f, y, paint); y += 30f
    paint.isFakeBoldText = false; paint.textSize = 12f
    canvas.drawText("Data: ${java.util.Date()}", 40f, y, paint); y += 30f

    paint.textSize = 16f; paint.isFakeBoldText = true
    canvas.drawText("🏆 Conquistas (${conquistas.size})", 40f, y, paint); y += 20f
    paint.isFakeBoldText = false; paint.textSize = 12f
    if (conquistas.isEmpty()) { canvas.drawText("Nenhuma ainda.", 60f, y, paint); y += 20f }
    else conquistas.forEach { c -> canvas.drawText("• ${c.replace("_", " ").uppercase()}", 60f, y, paint); y += 18f }

    y += 20f
    paint.textSize = 16f; paint.isFakeBoldText = true
    canvas.drawText("📊 Estatísticas", 40f, y, paint); y += 20f
    paint.isFakeBoldText = false; paint.textSize = 12f
    canvas.drawText("Espécies catalogadas: ${DinoData.REAIS.size}", 60f, y, paint); y += 18f

    doc.finishPage(page)
    val dir = File(ctx.filesDir, "relatorios").apply { mkdirs() }
    val file = File(dir, "relatorio_${System.currentTimeMillis()}.pdf")
    file.outputStream().use { doc.writeTo(it) }
    doc.close()
    return file
}