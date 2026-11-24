package com.devdenisviana.hidrataai

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacaoTest {

    @Test
    fun registroAceitaDeUmAteCincoMilMl() {
        assertTrue(registroValido(1))
        assertTrue(registroValido(5000))
    }

    @Test
    fun registroRecusaZeroNegativoEMuitoGrande() {
        assertFalse(registroValido(0))
        assertFalse(registroValido(-200))
        assertFalse(registroValido(5001))
    }

    @Test
    fun recipienteAceitaNomeEMlValidos() {
        assertTrue(recipienteValido("Minha garrafa", 650))
    }

    @Test
    fun recipienteRecusaNomeEmBrancoOuMuitoLongo() {
        assertFalse(recipienteValido("   ", 650))
        assertFalse(recipienteValido("Garrafa com nome muito grande mesmo", 650))
    }

    @Test
    fun recipienteRecusaMlInvalido() {
        assertFalse(recipienteValido("Garrafa", 0))
        assertFalse(recipienteValido("Garrafa", 6000))
    }

    @Test
    fun metaAceitaDeQuinhentosAteDezMilMl() {
        assertTrue(metaValida(500))
        assertTrue(metaValida(10_000))
    }

    @Test
    fun metaRecusaForaDoIntervalo() {
        assertFalse(metaValida(499))
        assertFalse(metaValida(10_001))
    }

    @Test
    fun pesoAceitaDeVinteATrezentosKg() {
        assertTrue(pesoValido(20))
        assertTrue(pesoValido(300))
    }

    @Test
    fun pesoRecusaForaDoIntervalo() {
        assertFalse(pesoValido(19))
        assertFalse(pesoValido(301))
    }
}