package com.dino.app.data.model

data class ResultadoQuiz(val timestamp: Long, val modo: String, val acertos: Int, val total: Int) {
    val percentual: Float get() = if (total == 0) 0f else acertos * 100f / total

    fun serializar() = "$timestamp|$modo|$acertos|$total"

    companion object {
        fun parseLista(texto: String?): List<ResultadoQuiz> = texto.orEmpty().lines().mapNotNull { linha ->
            val p = linha.split("|")
            if (p.size != 4) return@mapNotNull null
            ResultadoQuiz(
                p[0].toLongOrNull() ?: return@mapNotNull null,
                p[1],
                p[2].toIntOrNull() ?: return@mapNotNull null,
                p[3].toIntOrNull() ?: return@mapNotNull null
            )
        }
    }
}