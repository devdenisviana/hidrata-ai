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

    fun lembretesAtivos(): Boolean {
        return armazenamento.getBoolean(CHAVE_LEMBRETES_ATIVOS, false)
    }

    fun salvarLembretesAtivos(ativos: Boolean) {
        armazenamento.edit().putBoolean(CHAVE_LEMBRETES_ATIVOS, ativos).apply()
    }

    fun lerIntervaloHoras(): Int {
        return armazenamento.getInt(CHAVE_INTERVALO, INTERVALO_PADRAO_HORAS)
    }

    fun salvarIntervaloHoras(horas: Int) {
        armazenamento.edit().putInt(CHAVE_INTERVALO, horas).apply()
    }

    fun lerInicioHora(): Int {
        return armazenamento.getInt(CHAVE_INICIO, INICIO_PADRAO_HORA)
    }

    fun salvarInicioHora(hora: Int) {
        armazenamento.edit().putInt(CHAVE_INICIO, hora).apply()
    }

    fun lerFimHora(): Int {
        return armazenamento.getInt(CHAVE_FIM, FIM_PADRAO_HORA)
    }

    fun salvarFimHora(hora: Int) {
        armazenamento.edit().putInt(CHAVE_FIM, hora).apply()
    }

    private companion object {
        const val CHAVE_META = "meta_ml"
        const val CHAVE_PESO = "peso_kg"
        const val CHAVE_LEMBRETES_ATIVOS = "lembretes_ativos"
        const val CHAVE_INTERVALO = "lembrete_intervalo_horas"
        const val CHAVE_INICIO = "lembrete_inicio_hora"
        const val CHAVE_FIM = "lembrete_fim_hora"
    }
}