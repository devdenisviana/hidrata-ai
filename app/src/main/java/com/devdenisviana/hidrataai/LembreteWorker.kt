package com.devdenisviana.hidrataai

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.time.LocalDate
import kotlinx.coroutines.flow.first

class LembreteWorker(
    contexto: Context,
    parametros: WorkerParameters
) : CoroutineWorker(contexto, parametros) {

    override suspend fun doWork(): Result {
        val preferencias = Preferencias(applicationContext)
        if (!preferencias.lembretesAtivos()) return Result.success()

        val hoje = LocalDate.now()
        val registros = BancoHidrata.obter(applicationContext)
            .registroDao()
            .registrosDoPeriodo(inicioDoDiaMillis(hoje), inicioDoDiaMillis(hoje.plusDays(1)))
            .first()

        val total = registros.sumOf { it.ml }
        val meta = preferencias.lerMetaMl()

        if (deveAvisar(total, meta)) {
            mostrarLembrete(applicationContext, restanteMl(total, meta))
        }

        AgendadorLembretes.agendar(applicationContext)
        return Result.success()
    }
}