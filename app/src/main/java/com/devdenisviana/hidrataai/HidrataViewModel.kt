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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HidrataViewModel(application: Application) : AndroidViewModel(application) {

    private val banco = BancoHidrata.obter(application)
    private val registroDao = banco.registroDao()
    private val recipienteDao = banco.recipienteDao()
    private val preferencias = Preferencias(application)

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
        .flatMapLatest { (inicio, fim) -> registroDao.registrosDoPeriodo(inicio, fim) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalDeHojeMl: StateFlow<Int> = registrosDeHoje
        .map { lista -> lista.sumOf { it.ml } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val recipientes: StateFlow<List<Recipiente>> = recipienteDao.todos()
        .map { personalizados -> RECIPIENTES_PADRAO + personalizados }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RECIPIENTES_PADRAO)

    private val recipienteEscolhidoId = MutableStateFlow(ID_RECIPIENTE_INICIAL)

    val recipienteSelecionado: StateFlow<Recipiente> =
        combine(recipientes, recipienteEscolhidoId) { lista, id ->
            lista.firstOrNull { it.id == id } ?: RECIPIENTES_PADRAO.first()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RECIPIENTES_PADRAO.first())

    private val _metaMl = MutableStateFlow(preferencias.lerMetaMl())
    val metaMl: StateFlow<Int> = _metaMl

    private val _pesoKg = MutableStateFlow(preferencias.lerPesoKg())
    val pesoKg: StateFlow<Int> = _pesoKg

    fun escolherRecipiente(id: Long) {
        recipienteEscolhidoId.value = id
    }

    fun beber() {
        registrar(recipienteSelecionado.value.ml)
    }

    fun beberOutroValor(ml: Int) {
        if (registroValido(ml)) registrar(ml)
    }

    fun desfazerUltimo() {
        viewModelScope.launch { registroDao.desfazerUltimo() }
    }

    fun criarRecipiente(nome: String, ml: Int) {
        if (!recipienteValido(nome, ml)) return
        viewModelScope.launch {
            recipienteDao.inserir(Recipiente(nome = nome.trim(), ml = ml))
        }
    }

    fun apagarRecipiente(recipiente: Recipiente) {
        if (recipiente.id <= 0) return
        viewModelScope.launch { recipienteDao.apagar(recipiente) }
    }

    fun definirMeta(ml: Int) {
        if (!metaValida(ml)) return
        preferencias.salvarMetaMl(ml)
        _metaMl.value = ml
    }

    fun definirPeso(kg: Int) {
        if (!pesoValido(kg)) return
        preferencias.salvarPesoKg(kg)
        _pesoKg.value = kg
    }

    private fun registrar(ml: Int) {
        viewModelScope.launch {
            registroDao.inserir(RegistroAgua(horaMillis = System.currentTimeMillis(), ml = ml))
        }
    }
}