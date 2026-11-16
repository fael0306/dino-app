package com.dino.app.domain

import com.dino.app.data.DinoData
import com.dino.app.data.model.BatalhaPergunta
import com.dino.app.data.model.Dinosaur

object BatalhaGenerator {
    private val ordem = mapOf("Triássico" to 1, "Jurássico" to 2, "Cretáceo" to 3)

    fun gerar(d1: Dinosaur, d2: Dinosaur, qtd: Int = 5): List<BatalhaPergunta> {
        val res = mutableListOf<BatalhaPergunta>()
        val tentativas = mutableSetOf<String>()
        var i = 0
        while (res.size < qtd && i < qtd * 10) {
            val tipo = listOf("Peso", "Comprimento", "Altura", "Periodo").random()
            if (!tentativas.add(tipo + res.size)) { i++; continue }
            val p = when (tipo) {
                "Peso" -> atr(d1, d2, "Peso")
                "Comprimento" -> atr(d1, d2, "Comprimento")
                "Altura" -> atr(d1, d2, "Altura")
                "Periodo" -> per(d1, d2)
                else -> null
            }
            if (p != null) res.add(p)
            i++
        }
        while (res.size < qtd) res.add(atr(d1, d2, "Comprimento")!!)
        return res.take(qtd)
    }

    private fun atr(d1: Dinosaur, d2: Dinosaur, chave: String): BatalhaPergunta? {
        val v1 = when (chave) { "Peso" -> d1.Peso; "Comprimento" -> d1.Comprimento; else -> d1.Altura }
        val v2 = when (chave) { "Peso" -> d2.Peso; "Comprimento" -> d2.Comprimento; else -> d2.Altura }
        if (v1 == v2) return null
        val maior = if (v1 > v2) d1 else d2
        val menor = if (maior == d1) d2 else d1
        val u = if (chave == "Peso") "ton" else "m"
        val lbl = when (chave) { "Peso" -> "peso"; "Comprimento" -> "comprimento"; else -> "altura" }
        val dist = DinoData.REAIS.filter { it.Nome != d1.Nome && it.Nome != d2.Nome }.shuffled().take(2).map { it.Nome }
        val op = (listOf(maior.Nome, menor.Nome) + dist).shuffled()
        return BatalhaPergunta("Quem tem maior $lbl?", op, op.indexOf(maior.Nome), "${maior.Nome}: $v1 $u; ${menor.Nome}: $v2 $u.")
    }

    private fun per(d1: Dinosaur, d2: Dinosaur): BatalhaPergunta? {
        val p1 = ordem.entries.firstOrNull { d1.Periodo.contains(it.key) }?.value ?: return null
        val p2 = ordem.entries.firstOrNull { d2.Periodo.contains(it.key) }?.value ?: return null
        if (p1 == p2) return null
        val antigo = if (p1 < p2) d1 else d2
        val novo = if (antigo == d1) d2 else d1
        val dist = DinoData.REAIS.filter { it.Nome != d1.Nome && it.Nome != d2.Nome }.shuffled().take(2).map { it.Nome }
        val op = (listOf(antigo.Nome, novo.Nome) + dist).shuffled()
        return BatalhaPergunta("Quem viveu no período mais antigo?", op, op.indexOf(antigo.Nome), "${antigo.Nome}: ${antigo.Periodo}; ${novo.Nome}: ${novo.Periodo}.")
    }
}