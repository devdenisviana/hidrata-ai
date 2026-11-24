package com.devdenisviana.hidrataai

import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LembretesTest {

    private fun dia(hora: Int, minuto: Int = 0): LocalDateTime {
        return LocalDateTime.of(2026, 11, 24, hora, minuto)
    }

    @Test
    fun horariosDeDuasEmDuasHorasDasOitoAsVinteEDuas() {
        assertEquals(listOf(8, 10, 12, 14, 16, 18, 20, 22), horariosDoDia(8, 22, 2))
    }

    @Test
    fun horariosDeTresEmTresHorasParamNoUltimoQueCabe() {
        assertEquals(listOf(8, 11, 14, 17, 20), horariosDoDia(8, 22, 3))
    }

    @Test
    fun antesDoInicioOProximoEhOPrimeiroDoDia() {
        assertEquals(dia(8), proximoLembrete(dia(7, 30), 8, 22, 2))
    }

    @Test
    fun noExatoHorarioOProximoEhOSeguinte() {
        assertEquals(dia(10), proximoLembrete(dia(8), 8, 22, 2))
    }

    @Test
    fun entreDoisHorariosOProximoEhOSeguinte() {
        assertEquals(dia(12), proximoLembrete(dia(10, 1), 8, 22, 2))
    }

    @Test
    fun depoisDoUltimoOProximoEhOPrimeiroDeAmanha() {
        val amanha = LocalDateTime.of(2026, 11, 25, 8, 0)
        assertEquals(amanha, proximoLembrete(dia(22), 8, 22, 2))
        assertEquals(amanha, proximoLembrete(dia(23, 30), 8, 22, 2))
    }

    @Test
    fun configuracaoValidaAceitaJanelaEIntervaloCertos() {
        assertTrue(configuracaoLembreteValida(8, 22, 2))
    }

    @Test
    fun configuracaoInvalidaRecusaInicioNaoMenorQueFimEIntervaloFora() {
        assertFalse(configuracaoLembreteValida(22, 8, 2))
        assertFalse(configuracaoLembreteValida(8, 8, 2))
        assertFalse(configuracaoLembreteValida(8, 22, 5))
        assertFalse(configuracaoLembreteValida(-1, 22, 2))
        assertFalse(configuracaoLembreteValida(8, 24, 2))
    }

    @Test
    fun soAvisaQuandoAindaNaoBateuAMeta() {
        assertTrue(deveAvisar(1500, 2000))
        assertFalse(deveAvisar(2000, 2000))
        assertFalse(deveAvisar(2500, 2000))
    }
}