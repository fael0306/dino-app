package com.dino.app

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File

/** Resultado de uma exportação. */
data class ArquivoSalvo(
    val nome: String,
    val mime: String,
    /** URI do FileProvider, usada para abrir ou compartilhar o arquivo. */
    val uri: Uri,
    /** true quando uma cópia também foi gravada em Downloads/PaleoLab (Android 10+). */
    val emDownloads: Boolean
)

/**
 * Grava os arquivos gerados pela página (relatório PDF, backup JSON).
 * Uma cópia fica na pasta interna do app (para abrir/compartilhar) e, no
 * Android 10 ou superior, outra vai para Downloads/PaleoLab sem pedir permissão.
 */
class SalvadorArquivos(private val context: Context) {

    fun salvar(nomeOriginal: String, mimeOriginal: String?, conteudo: ByteArray): ArquivoSalvo {
        val nome = NomesArquivo.seguro(nomeOriginal)
        val mime = NomesArquivo.mimeValido(mimeOriginal, nome)

        val pasta = File(context.filesDir, "exports").apply { mkdirs() }
        val arquivo = File(pasta, nome)
        arquivo.writeBytes(conteudo)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", arquivo)

        val emDownloads = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            salvarEmDownloads(nome, mime, conteudo)
        } else {
            false
        }
        return ArquivoSalvo(nome, mime, uri, emDownloads)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun salvarEmDownloads(nome: String, mime: String, conteudo: ByteArray): Boolean {
        val resolver = context.contentResolver
        val valores = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, nome)
            put(MediaStore.MediaColumns.MIME_TYPE, mime)
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/PaleoLab")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        var uri: Uri? = null
        return try {
            val destino = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, valores)
                ?: return false
            uri = destino
            val saida = resolver.openOutputStream(destino) ?: throw IllegalStateException("Sem fluxo de saída")
            saida.use { it.write(conteudo) }
            valores.clear()
            valores.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(destino, valores, null, null)
            true
        } catch (e: Exception) {
            uri?.let { runCatching { resolver.delete(it, null, null) } }
            false
        }
    }
}
