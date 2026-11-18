package com.dino.app

/** Regras puras para nomes e tipos de arquivo (testáveis sem Android). */
object NomesArquivo {
    private val proibidos = Regex("[\\\\/:*?\"<>|\\x00-\\x1F]")
    private const val TAMANHO_MAXIMO = 120

    /** Remove caracteres inválidos em nomes de arquivo e limita o tamanho. */
    fun seguro(nome: String, padrao: String = "arquivo"): String {
        val limpo = nome.replace(proibidos, "_").trim().trimStart('.').take(TAMANHO_MAXIMO).trim()
        return limpo.ifBlank { padrao }
    }

    /** Usa o tipo informado pela página ou deduz pela extensão. */
    fun mimeValido(mime: String?, nome: String): String = when {
        !mime.isNullOrBlank() && mime.contains('/') -> mime
        nome.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
        nome.endsWith(".json", ignoreCase = true) -> "application/json"
        else -> "application/octet-stream"
    }
}
