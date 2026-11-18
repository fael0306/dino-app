package com.dino.app

import org.junit.Assert.assertEquals
import org.junit.Test

class NomesArquivoTest {

    @Test
    fun mantemNomesValidos() {
        assertEquals("relatorio_paleolab.pdf", NomesArquivo.seguro("relatorio_paleolab.pdf"))
    }

    @Test
    fun trocaCaracteresProibidos() {
        assertEquals("a_b_c_d.pdf", NomesArquivo.seguro("a/b:c\\d.pdf"))
        assertEquals("x_y.json", NomesArquivo.seguro("x\u0000y.json"))
    }

    @Test
    fun removePontosNoInicio() {
        assertEquals("oculto.json", NomesArquivo.seguro("..oculto.json"))
    }

    @Test
    fun nomeVazioUsaPadrao() {
        assertEquals("arquivo", NomesArquivo.seguro("   "))
        assertEquals("backup", NomesArquivo.seguro("", padrao = "backup"))
    }

    @Test
    fun limitaTamanho() {
        assertEquals(120, NomesArquivo.seguro("x".repeat(300)).length)
    }

    @Test
    fun deduzTipoPelaExtensao() {
        assertEquals("application/pdf", NomesArquivo.mimeValido("", "relatorio.PDF"))
        assertEquals("application/json", NomesArquivo.mimeValido(null, "conquistas.json"))
        assertEquals("application/octet-stream", NomesArquivo.mimeValido("invalido", "dados.bin"))
    }

    @Test
    fun respeitaTipoInformado() {
        assertEquals("application/pdf", NomesArquivo.mimeValido("application/pdf", "sem_extensao"))
    }
}
