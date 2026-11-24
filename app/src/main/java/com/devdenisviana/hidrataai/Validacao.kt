package com.devdenisviana.hidrataai

const val MAX_ML = 5000
const val MAX_NOME_RECIPIENTE = 20

fun registroValido(ml: Int): Boolean {
    return ml in 1..MAX_ML
}

fun recipienteValido(nome: String, ml: Int): Boolean {
    val nomeLimpo = nome.trim()
    return nomeLimpo.isNotEmpty() &&
            nomeLimpo.length <= MAX_NOME_RECIPIENTE &&
            registroValido(ml)
}