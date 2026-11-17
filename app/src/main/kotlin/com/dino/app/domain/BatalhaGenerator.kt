package com.dino.app.domain

import com.dino.app.data.model.BatalhaPergunta
import com.dino.app.data.model.Dinosaur

object BatalhaGenerator {
    private val ordem = mapOf("Triássico" to 1, "Jurássico" to 2, "Cretáceo" to 3)
    private val dietas = listOf("Carnívoro", "Herbívoro", "Piscívoro")

    /** Gera [qtd] perguntas sobre o duelo. Nunca lança exceção; devolve lista vazia só se nada for possível. */
    fun gerar(d1: Dinosaur, d2: Dinosaur, qtd: Int = 5): List<BatalhaPergunta> {
        val pool = listOfNotNull(
            atributo(d1, d2, "Peso"), atributo(d1, d2, "Comprimento"), atributo(d1, d2, "Altura"),
            periodo(d1, d2), dieta(d1), dieta(d2)
        ).shuffled()
        if (pool.isEmpty()) return emptyList()
        return List(qtd) { pool[it % pool.size] }
    }

    private fun atributo(d1: Dinosaur, d2: Dinosaur, chave: String): BatalhaPergunta? {
        val (v1, v2) = when (chave) {
            "Peso" -> d1.Peso to d2.Peso
            "Comprimento" -> d1.Comprimento to d2.Comprimento
            else -> d1.Altura to d2.Altura
        }
        if (v1 == v2) return null
        val unidade = if (chave == "Peso") "ton" else "m"
        val rotulo = chave.lowercase()
        val vencedor = if (v1 > v2) d1 else d2
        val perdedor = if (v1 > v2) d2 else d1
        val opcoes = listOf(d1.Nome, d2.Nome).shuffled()
        return BatalhaPergunta(
            "Entre ${d1.Nome} e ${d2.Nome}, quem tem maior $rotulo?",
            opcoes, opcoes.indexOf(vencedor.Nome),
            "${vencedor.Nome}: ${maxOf(v1, v2)} $unidade; ${perdedor.Nome}: ${minOf(v1, v2)} $unidade."
        )
    }

    private fun periodo(d1: Dinosaur, d2: Dinosaur): BatalhaPergunta? {
        val p1 = ordem.entries.firstOrNull { d1.Periodo.contains(it.key) }?.value ?: return null
        val p2 = ordem.entries.firstOrNull { d2.Periodo.contains(it.key) }?.value ?: return null
        if (p1 == p2) return null
        val antigo = if (p1 < p2) d1 else d2
        val novo = if (p1 < p2) d2 else d1
        val opcoes = listOf(d1.Nome, d2.Nome).shuffled()
        return BatalhaPergunta(
            "Entre ${d1.Nome} e ${d2.Nome}, quem viveu no período mais antigo?",
            opcoes, opcoes.indexOf(antigo.Nome),
            "${antigo.Nome}: ${antigo.Periodo}; ${novo.Nome}: ${novo.Periodo}."
        )
    }

    private fun dieta(d: Dinosaur): BatalhaPergunta? {
        val idx = dietas.indexOf(d.Dieta)
        if (idx < 0) return null
        return BatalhaPergunta("Qual é a dieta de ${d.Nome}?", dietas, idx, "${d.Nome}: ${d.Dieta}.")
    }
}