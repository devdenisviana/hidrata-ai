package com.devdenisviana.hidrataai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class HistoricoViewModel(application: Application) : AndroidViewModel(application) {

    private val registroDao = BancoHidrata.obter(application).registroDao()

    private val diaDeHoje = flow {
        while (true) {
            emit(LocalDate.now())
            delay(millisAteOProximoDia(System.currentTimeMillis()) + 1_000)
        }
    }

    val ultimosSeteDias: StateFlow<List<TotalDoDia>> = diaDeHoje
        .flatMapLatest { hoje ->
            val dias = ultimosDias(hoje, 7)
            registroDao
                .registrosDoPeriodo(
                    inicioDoDiaMillis(dias.first()),
                    inicioDoDiaMillis(hoje.plusDays(1))
                )
                .map { registros -> totaisPorDia(registros, dias) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}