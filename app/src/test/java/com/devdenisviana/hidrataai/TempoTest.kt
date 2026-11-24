package com.devdenisviana.hidrataai

import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class TempoTest {

    @Test
    fun inicioDoDiaEmUtcComecaNaMeiaNoite() {
        val inicio = inicioDoDiaMillis(LocalDate.of(1970, 1, 2), ZoneOffset.UTC)
        assertEquals(86_400_000L, inicio)
    }

    @Test
    fun as23HorasFaltaUmaHoraParaOProximoDia() {
        val vinteETresHoras = 23 * 3_600_000L
        assertEquals(3_600_000L, millisAteOProximoDia(vinteETresHoras, ZoneOffset.UTC))
    }
}