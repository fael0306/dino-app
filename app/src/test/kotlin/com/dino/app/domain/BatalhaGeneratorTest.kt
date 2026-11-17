package com.dino.app.domain

import com.dino.app.data.DinoData
import org.junit.Assert.*
import org.junit.Test

class BatalhaGeneratorTest {
    @Test fun todosOsParesGeramPerguntasValidas() {
        val lista = DinoData.REAIS
        for (a in lista) for (b in lista) {
            if (a.Nome == b.Nome) continue
            val qs = BatalhaGenerator.gerar(a, b, 5)
            assertEquals(5, qs.size)
            qs.forEach { q ->
                assertTrue(q.resposta in q.opcoes.indices)
                assertEquals(q.opcoes.size, q.opcoes.toSet().size)
            }
        }
    }

    @Test fun dinossaurosComAtributosIguaisNaoQuebram() {
        val a = DinoData.REAIS[0]
        val b = a.copy(Nome = "Clone")
        val qs = BatalhaGenerator.gerar(a, b, 5)
        assertEquals(5, qs.size)
    }

    @Test fun respostaDePesoApontaParaOMaisPesado() {
        val rex = DinoData.REAIS.first { it.Nome.startsWith("Tyrannosaurus") }
        val raptor = DinoData.REAIS.first { it.Nome.startsWith("Velociraptor") }
        repeat(50) {
            BatalhaGenerator.gerar(rex, raptor, 6)
                .filter { "maior peso" in it.pergunta }
                .forEach { assertEquals(rex.Nome, it.opcoes[it.resposta]) }
        }
    }

    @Test fun respostaDePeriodoApontaParaOMaisAntigo() {
        val jurassico = DinoData.REAIS.first { it.Periodo.contains("Jurássico") }
        val cretaceo = DinoData.REAIS.first { it.Periodo.contains("Cretáceo") }
        repeat(50) {
            BatalhaGenerator.gerar(cretaceo, jurassico, 6)
                .filter { "mais antigo" in it.pergunta }
                .forEach { assertEquals(jurassico.Nome, it.opcoes[it.resposta]) }
        }
    }
}
