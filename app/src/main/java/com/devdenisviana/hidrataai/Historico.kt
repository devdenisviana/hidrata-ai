package com.devdenisviana.hidrataai

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class TotalDoDia(val dia: LocalDate, val ml: Int)

fun ultimosDias(hoje: LocalDate, quantidade: Int): List<LocalDate> {
    return (quantidade - 1 downTo 0).map { hoje.minusDays(it.toLong()) }
}

fun totaisPorDia(
    registros: List<RegistroAgua>,
    dias: List<LocalDate>,
    zona: ZoneId = ZoneId.systemDefault()
): List<TotalDoDia> {
    val somas = registros
        .groupBy { Instant.ofEpochMilli(it.horaMillis).atZone(zona).toLocalDate() }
        .mapValues { (_, doDia) -> doDia.sumOf { it.ml } }
    return dias.map { dia -> TotalDoDia(dia, somas[dia] ?: 0) }
}

fun mediaDiaria(totais: List<TotalDoDia>): Int {
    if (totais.isEmpty()) return 0
    return totais.sumOf { it.ml } / totais.size
}

fun diasQueBateramMeta(totais: List<TotalDoDia>, metaMl: Int): Int {
    return totais.count { it.ml >= metaMl }
}