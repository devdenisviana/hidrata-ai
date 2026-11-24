package com.devdenisviana.hidrataai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipienteTest {

    @Test
    fun ateTrezentosECinquentaMlEhCopo() {
        assertEquals(TipoRecipiente.COPO, tipoDoRecipiente(200))
        assertEquals(TipoRecipiente.COPO, tipoDoRecipiente(350))
    }

    @Test
    fun acimaDeTrezentosECinquentaMlEhGarrafa() {
        assertEquals(TipoRecipiente.GARRAFA, tipoDoRecipiente(351))
        assertEquals(TipoRecipiente.GARRAFA, tipoDoRecipiente(750))
    }

    @Test
    fun recipientesPadraoTemIdsNegativosEUnicos() {
        val ids = RECIPIENTES_PADRAO.map { it.id }
        assertTrue(ids.all { it < 0 })
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun recipienteInicialExisteNaListaPadrao() {
        assertTrue(RECIPIENTES_PADRAO.any { it.id == ID_RECIPIENTE_INICIAL })
    }
}