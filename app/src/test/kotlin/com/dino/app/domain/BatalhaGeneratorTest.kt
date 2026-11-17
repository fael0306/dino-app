package com.dino.app.domain

import org.junit.Assert.*
import org.junit.Test

class SimulacoesTest {
    @Test fun massaCresceComACircunferencia() {
        assertTrue(Simulacoes.calcularMassaTon(60.0, true) > Simulacoes.calcularMassaTon(40.0, true))
        assertTrue(Simulacoes.calcularMassaTon(60.0, false) > Simulacoes.calcularMassaTon(40.0, false))
    }

    @Test fun massaECircunferenciaSaoInversas() {
        for (bip in listOf(true, false)) {
            val c = 55.0
            val volta = Simulacoes.circFemoralPorMassa(Simulacoes.calcularMassaTon(c, bip), bip)
            assertEquals(c, volta, 1e-6)
        }
    }

    @Test fun circunferenciaParaMassaNaoPositivaEZero() {
        assertEquals(0.0, Simulacoes.circFemoralPorMassa(0.0, true), 0.0)
        assertEquals(0.0, Simulacoes.circFemoralPorMassa(-1.0, false), 0.0)
    }

    @Test fun icnofosseisSaoIdentificados() {
        assertEquals("Eubrontes", Simulacoes.identificarIcnogenus(3, true, "grande", "alongada", null))
        assertEquals("Grallator", Simulacoes.identificarIcnogenus(3, false, "pequeno", null, null))
        assertEquals("Anomoepus", Simulacoes.identificarIcnogenus(4, false, null, null, "larga"))
        assertEquals("Chirotherium", Simulacoes.identificarIcnogenus(4, true, null, null, null))
        assertEquals("Desconhecido", Simulacoes.identificarIcnogenus(3, true, null, null, null))
    }

    @Test fun simulacaoTemTamanhoEsperadoEValoresNaoNegativos() {
        val r = Simulacoes.simularExtincao(50.0, 50.0, 10)
        assertEquals(200, r.tempo.size)
        assertEquals(200, r.plantas.size)
        assertTrue((r.plantas + r.herbivoros + r.carnivoros).all { it >= 0.0 && it.isFinite() })
    }

    @Test fun csvTemCabecalhoELinhas() {
        val r = Simulacoes.simularExtincao(10.0, 10.0, 2)
        val linhas = Simulacoes.paraCsv(r).trim().lines()
        assertEquals("tempo_anos,plantas,herbivoros,carnivoros", linhas.first())
        assertEquals(r.tempo.size + 1, linhas.size)
        assertEquals(4, linhas[1].split(",").size)
    }
}