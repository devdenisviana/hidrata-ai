package com.devdenisviana.hidrataai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HidrataViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BancoHidrata.obter(application).registroDao()

    private val periodoDeHoje = flow {
        while (true) {
            val hoje = LocalDate.now()
            val inicio = inicioDoDiaMillis(hoje)
            val fim = inicioDoDiaMillis(hoje.plusDays(1))
            emit(inicio to fim)
            delay(millisAteOProximoDia(System.currentTimeMillis()) + 1_000)
        }
    }

    val registrosDeHoje: StateFlow<List<RegistroAgua>> = periodoDeHoje
        .flatMapLatest { (inicio, fim) -> dao.registrosDoPeriodo(inicio, fim) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalDeHojeMl: StateFlow<Int> = registrosDeHoje
        .map { lista -> lista.sumOf { it.ml } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    private val _copoMl = MutableStateFlow(200)
    val copoMl: StateFlow<Int> = _copoMl

    fun escolherCopo(ml: Int) {
        _copoMl.value = ml
    }

    fun beber() {
        val ml = _copoMl.value
        viewModelScope.launch {
            dao.inserir(RegistroAgua(horaMillis = System.currentTimeMillis(), ml = ml))
        }
    }

    fun desfazerUltimo() {
        viewModelScope.launch { dao.desfazerUltimo() }
    }
}