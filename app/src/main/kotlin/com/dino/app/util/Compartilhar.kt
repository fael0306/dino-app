package com.dino.app.util

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun Context.pastaExportacao(): File = File(filesDir, "exports").apply { mkdirs() }

/** Abre o seletor de compartilhamento do Android. Retorna false se algo falhar (ex.: FileProvider não configurado). */
fun Context.compartilharArquivo(arquivo: File, mime: String, titulo: String): Boolean = runCatching {
    val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", arquivo)
    val envio = Intent(Intent.ACTION_SEND).apply {
        type = mime
        putExtra(Intent.EXTRA_STREAM, uri)
        clipData = ClipData.newRawUri("", uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(envio, titulo).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}.isSuccess