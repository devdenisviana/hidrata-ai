package com.devdenisviana.hidrataai

import java.time.LocalDateTime

val INTERVALOS_LEMBRETE_HORAS = listOf(1, 2, 3, 4)
const val INTERVALO_PADRAO_HORAS = 2
const val INICIO_PADRAO_HORA = 8
const val FIM_PADRAO_HORA = 22

fun configuracaoLembreteValida(inicioHora: Int, fimHora: Int, intervaloHoras: Int): Boolean {
    return inicioHora in 0..23 &&
            fimHora in 0..23 &&
            inicioHora < fimHora &&
            intervaloHoras in INTERVALOS_LEMBRETE_HORAS
}

fun horariosDoDia(inicioHora: Int, fimHora: Int, intervaloHoras: Int): List<Int> {
    return (inicioHora..fimHora step intervaloHoras).toList()
}

fun proximoLembrete(
    agora: LocalDateTime,
    inicioHora: Int,
    fimHora: Int,
    intervaloHoras: Int
): LocalDateTime {
    val horarios = horariosDoDia(inicioHora, fimHora, intervaloHoras)
    val hoje = agora.toLocalDate()
    val proximoHoje = horarios
        .map { hora -> hoje.atTime(hora, 0) }
        .firstOrNull { horario -> horario.isAfter(agora) }
    return proximoHoje ?: hoje.plusDays(1).atTime(horarios.first(), 0)
}

fun deveAvisar(totalMl: Int, metaMl: Int): Boolean {
    return totalMl < metaMl
}