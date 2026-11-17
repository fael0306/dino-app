package com.dino.app.data

data class ConquistaInfo(val id: String, val nome: String, val emoji: String)

object Conquistas {
    val TODAS = listOf(
        ConquistaInfo("quiz_facil", "Quiz Fácil", "🥇"), ConquistaInfo("quiz_medio", "Quiz Médio", "🥈"),
        ConquistaInfo("quiz_dificil", "Quiz Difícil", "🥉"), ConquistaInfo("explorador_escala", "Explorador", "🔍"),
        ConquistaInfo("detetive_icno", "Detetive", "🕵️"), ConquistaInfo("climaturista", "Climaturista", "🌤️"),
        ConquistaInfo("batalha_mestre", "Batalha Mestre", "⚔️")
    )

    fun porId(id: String): ConquistaInfo? = TODAS.firstOrNull { it.id == id }
}
