package com.devdenisviana.hidrataai

const val ML_POR_KG = 35
const val ARREDONDAMENTO_META_ML = 50
const val META_PADRAO_ML = 2000

fun metaSugeridaPorPeso(pesoKg: Int): Int {
    val bruto = pesoKg * ML_POR_KG
    val metade = ARREDONDAMENTO_META_ML / 2
    return ((bruto + metade) / ARREDONDAMENTO_META_ML) * ARREDONDAMENTO_META_ML
}

fun fracaoDaMeta(consumidoMl: Int, metaMl: Int): Float {
    if (metaMl <= 0) return 0f
    return (consumidoMl.toFloat() / metaMl).coerceIn(0f, 1f)
}

fun restanteMl(consumidoMl: Int, metaMl: Int): Int {
    return (metaMl - consumidoMl).coerceAtLeast(0)
}