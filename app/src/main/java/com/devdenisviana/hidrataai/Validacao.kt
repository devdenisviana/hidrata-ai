package com.devdenisviana.hidrataai

const val MAX_ML = 5000
const val MAX_NOME_RECIPIENTE = 20
const val META_MIN_ML = 500
const val META_MAX_ML = 10_000
const val PESO_MIN_KG = 20
const val PESO_MAX_KG = 300

fun registroValido(ml: Int): Boolean {
    return ml in 1..MAX_ML
}

fun recipienteValido(nome: String, ml: Int): Boolean {
    val nomeLimpo = nome.trim()
    return nomeLimpo.isNotEmpty() &&
            nomeLimpo.length <= MAX_NOME_RECIPIENTE &&
            registroValido(ml)
}

fun metaValida(ml: Int): Boolean {
    return ml in META_MIN_ML..META_MAX_ML
}

fun pesoValido(kg: Int): Boolean {
    return kg in PESO_MIN_KG..PESO_MAX_KG
}