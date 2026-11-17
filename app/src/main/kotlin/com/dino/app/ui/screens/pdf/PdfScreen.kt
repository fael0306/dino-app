package com.dino.app.ui.screens.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dino.app.data.Conquistas
import com.dino.app.data.DinoData
import com.dino.app.data.model.ResultadoQuiz
import com.dino.app.paleoApp
import com.dino.app.util.compartilharArquivo
import com.dino.app.util.pastaExportacao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PdfScreen() {
    val ctx = LocalContext.current
    val prefs = ctx.paleoApp.prefs
    val escopo = rememberCoroutineScope()
    var status by remember { mutableStateOf<String?>(null) }
    var arquivo by remember { mutableStateOf<File?>(null) }
    val conquistas by prefs.conquistasFlow.collectAsState(initial = emptySet())
    val historico by prefs.historicoFlow.collectAsState(initial = emptyList())

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("📄 Exportar Relatório", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            escopo.launch {
                status = "Gerando..."
                arquivo = withContext(Dispatchers.IO) { gerarPdf(ctx, conquistas, historico) }
                status = "✅ Relatório gerado."
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Gerar PDF") }
        status?.let { Spacer(Modifier.height(8.dp)); Text(it, style = MaterialTheme.typography.bodyMedium) }
        arquivo?.let { f ->
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = {
                if (!ctx.compartilharArquivo(f, "application/pdf", "Compartilhar relatório")) status = "Não foi possível compartilhar o PDF."
            }, modifier = Modifier.fillMaxWidth()) { Text("📤 Compartilhar") }
        }
    }
}

private class PdfBuilder {
    private val doc = PdfDocument()
    private var numero = 0
    private lateinit var page: PdfDocument.Page
    private lateinit var canvas: Canvas
    private var y = 0f
    private val paint = Paint().apply { isAntiAlias = true }

    init { novaPagina() }

    private fun novaPagina() {
        if (numero > 0) doc.finishPage(page)
        numero++
        page = doc.startPage(PdfDocument.PageInfo.Builder(LARGURA, ALTURA, numero).create())
        canvas = page.canvas
        y = MARGEM
    }

    /** Escreve texto com quebra de linha automática e paginação. */
    fun texto(t: String, tamanho: Float = 12f, negrito: Boolean = false, recuo: Float = 0f, espaco: Float = 4f) {
        paint.textSize = tamanho; paint.isFakeBoldText = negrito
        val largura = LARGURA - 2 * MARGEM - recuo
        var resto = t
        do {
            val n = if (resto.isEmpty()) 0 else paint.breakText(resto, true, largura, null).coerceAtLeast(1)
            if (y + tamanho > ALTURA - MARGEM) { novaPagina(); paint.textSize = tamanho; paint.isFakeBoldText = negrito }
            canvas.drawText(resto.substring(0, n), MARGEM + recuo, y, paint)
            y += tamanho * 1.3f
            resto = resto.substring(n)
        } while (resto.isNotEmpty())
        y += espaco
    }

    fun salvar(destino: File) {
        doc.finishPage(page)
        destino.outputStream().use { doc.writeTo(it) }
        doc.close()
    }

    companion object { const val LARGURA = 595; const val ALTURA = 842; const val MARGEM = 40f }
}

private fun gerarPdf(ctx: Context, conquistas: Set<String>, historico: List<ResultadoQuiz>): File {
    val pdf = PdfBuilder()
    val fmt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))

    pdf.texto("PaleoLab - Relatório Científico", 22f, negrito = true, espaco = 6f)
    pdf.texto("Gerado em ${fmt.format(Date())}", 11f, espaco = 14f)

    pdf.texto("Conquistas (${conquistas.size}/${Conquistas.TODAS.size})", 16f, negrito = true)
    if (conquistas.isEmpty()) pdf.texto("Nenhuma ainda.", recuo = 16f)
    else Conquistas.TODAS.filter { it.id in conquistas }.forEach { pdf.texto("• ${it.nome}", recuo = 16f, espaco = 1f) }

    pdf.texto("", 8f)
    pdf.texto("Quiz", 16f, negrito = true)
    if (historico.isEmpty()) pdf.texto("Nenhuma partida registrada.", recuo = 16f)
    else {
        val media = historico.map { it.percentual }.average()
        pdf.texto("Partidas: ${historico.size} | média de acertos: ${"%.0f".format(media)}%", recuo = 16f)
        historico.takeLast(10).reversed().forEach {
            pdf.texto("${fmt.format(Date(it.timestamp))} - ${it.modo}: ${it.acertos}/${it.total}", 11f, recuo = 16f, espaco = 1f)
        }
    }

    pdf.texto("", 8f)
    pdf.texto("Espécies catalogadas (${DinoData.REAIS.size})", 16f, negrito = true)
    DinoData.REAIS.forEach {
        pdf.texto("${it.Nome} - ${it.Periodo}, ${it.Dieta}, ${it.Comprimento} m, ${it.Peso} ton", 11f, recuo = 16f, espaco = 1f)
    }

    val destino = File(ctx.pastaExportacao(), "relatorio_${System.currentTimeMillis()}.pdf")
    pdf.salvar(destino)
    return destino
}