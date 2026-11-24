package com.devdenisviana.hidrataai

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun inicioDoDiaMillis(dia: LocalDate, zona: ZoneId = ZoneId.systemDefault()): Long {
    return dia.atStartOfDay(zona).toInstant().toEpochMilli()
}

fun millisAteOProximoDia(agoraMillis: Long, zona: ZoneId = ZoneId.systemDefault()): Long {
    val hoje = Instant.ofEpochMilli(agoraMillis).atZone(zona).toLocalDate()
    return inicioDoDiaMillis(hoje.plusDays(1), zona) - agoraMillis
}