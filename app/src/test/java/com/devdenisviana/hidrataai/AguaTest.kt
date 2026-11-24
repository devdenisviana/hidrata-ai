package com.devdenisviana.hidrataai

import org.junit.Assert.assertEquals
import org.junit.Test

class AguaTest {

    @Test
    fun metaPorPesoUsaTrintaECincoMlPorKg() {
        assertEquals(2450, metaSugeridaPorPeso(70))
    }

    @Test
    fun metaPorPesoArredondaParaMultiploDeCinquenta() {
        assertEquals(2500, metaSugeridaPorPeso(72))
        assertEquals(2550, metaSugeridaPorPeso(73))
    }

    @Test
    fun fracaoDaMetaCalculaOProgresso() {
        assertEquals(0.25f, fracaoDaMeta(500, 2000), 0.0001f)
    }

    @Test
    fun fracaoDaMetaNaoPassaDeUm() {
        assertEquals(1f, fracaoDaMeta(3000, 2000), 0.0001f)
    }

    @Test
    fun fracaoDaMetaComMetaZeroEhZero() {
        assertEquals(0f, fracaoDaMeta(500, 0), 0.0001f)
    }

    @Test
    fun restanteNuncaEhNegativo() {
        assertEquals(1500, restanteMl(500, 2000))
        assertEquals(0, restanteMl(2500, 2000))
    }
}
