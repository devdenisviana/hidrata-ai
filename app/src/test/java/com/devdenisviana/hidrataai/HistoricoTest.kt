package com.devdenisviana.hidrataai

import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class HistoricoTest {

    private val diaEmMillis = 86_400_000L

    @Test
    fun ultimosDiasVemEmOrdemCrescenteTerminandoHoje() {
        val dias = ultimosDias(LocalDate.of(2026, 11, 24), 3)
        assertEquals(
            listOf(
                LocalDate.of(2026, 11, 22),
                LocalDate.of(2026, 11, 23),
                LocalDate.of(2026, 11, 24)
            ),
            dias
        )
    }

    @Test
    fun totaisPorDiaSomaCadaDiaEPreencheZeros() {
        val registros = listOf(
            RegistroAgua(id = 1, horaMillis = diaEmMillis + 1_000, ml = 200),
            RegistroAgua(id = 2, horaMillis = diaEmMillis + 2_000, ml = 300),
            RegistroAgua(id = 3, horaMillis = 1_000, ml = 500)
        )
        val dias = ultimosDias(LocalDate.of(1970, 1, 3), 3)

        val totais = totaisPorDia(registros, dias, ZoneOffset.UTC)

        assertEquals(listOf(500, 500, 0), totais.map { it.ml })
    }

    @Test
    fun mediaDiariaDivideOTotalPeloNumeroDeDias() {
        val totais = listOf(
            TotalDoDia(LocalDate.of(2026, 11, 22), 1000),
            TotalDoDia(LocalDate.of(2026, 11, 23), 2000),
            TotalDoDia(LocalDate.of(2026, 11, 24), 0)
        )
        assertEquals(1000, mediaDiaria(totais))
    }

    @Test
    fun mediaDiariaSemDiasEhZero() {
        assertEquals(0, mediaDiaria(emptyList()))
    }

    @Test
    fun diasQueBateramMetaContaIgualOuMaior() {
        val totais = listOf(
            TotalDoDia(LocalDate.of(2026, 11, 22), 2000),
            TotalDoDia(LocalDate.of(2026, 11, 23), 1999),
            TotalDoDia(LocalDate.of(2026, 11, 24), 2500)
        )
        assertEquals(2, diasQueBateramMeta(totais, 2000))
    }
}