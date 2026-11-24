package com.devdenisviana.hidrataai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LembretesViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencias = Preferencias(application)
    private val registroDao = BancoHidrata.obter(application).registroDao()

    private val _ativos = MutableStateFlow(preferencias.lembretesAtivos())
    val ativos: StateFlow<Boolean> = _ativos

    private val _intervaloHoras = MutableStateFlow(preferencias.lerIntervaloHoras())
    val intervaloHoras: StateFlow<Int> = _intervaloHoras

    private val _inicioHora = MutableStateFlow(preferencias.lerInicioHora())
    val inicioHora: StateFlow<Int> = _inicioHora

    private val _fimHora = MutableStateFlow(preferencias.lerFimHora())
    val fimHora: StateFlow<Int> = _fimHora

    fun definirAtivos(ativos: Boolean) {
        preferencias.salvarLembretesAtivos(ativos)
        _ativos.value = ativos
        reagendar()
    }

    fun definirIntervalo(horas: Int) {
        if (!configuracaoLembreteValida(_inicioHora.value, _fimHora.value, horas)) return
        preferencias.salvarIntervaloHoras(horas)
        _intervaloHoras.value = horas
        reagendar()
    }

    fun definirInicio(hora: Int) {
        if (!configuracaoLembreteValida(hora, _fimHora.value, _intervaloHoras.value)) return
        preferencias.salvarInicioHora(hora)
        _inicioHora.value = hora
        reagendar()
    }

    fun definirFim(hora: Int) {
        if (!configuracaoLembreteValida(_inicioHora.value, hora, _intervaloHoras.value)) return
        preferencias.salvarFimHora(hora)
        _fimHora.value = hora
        reagendar()
    }

    fun enviarTeste() {
        viewModelScope.launch {
            val hoje = LocalDate.now()
            val total = registroDao
                .registrosDoPeriodo(inicioDoDiaMillis(hoje), inicioDoDiaMillis(hoje.plusDays(1)))
                .first()
                .sumOf { it.ml }
            mostrarLembrete(getApplication(), restanteMl(total, preferencias.lerMetaMl()))
        }
    }

    private fun reagendar() {
        AgendadorLembretes.agendar(getApplication())
    }
}