package com.devdenisviana.hidrataai

import android.content.Context

class Preferencias(contexto: Context) {

    private val armazenamento = contexto.applicationContext
        .getSharedPreferences("hidrata_prefs", Context.MODE_PRIVATE)

    fun lerMetaMl(): Int {
        return armazenamento.getInt(CHAVE_META, META_PADRAO_ML)
    }

    fun salvarMetaMl(ml: Int) {
        armazenamento.edit().putInt(CHAVE_META, ml).apply()
    }

    fun lerPesoKg(): Int {
        return armazenamento.getInt(CHAVE_PESO, 0)
    }

    fun salvarPesoKg(kg: Int) {
        armazenamento.edit().putInt(CHAVE_PESO, kg).apply()
    }

    private companion object {
        const val CHAVE_META = "meta_ml"
        const val CHAVE_PESO = "peso_kg"
    }
}