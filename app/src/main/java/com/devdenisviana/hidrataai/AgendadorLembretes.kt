package com.devdenisviana.hidrataai

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object AgendadorLembretes {

    private const val NOME_DO_TRABALHO = "lembrete_agua"

    fun agendar(contexto: Context) {
        val preferencias = Preferencias(contexto)
        if (!preferencias.lembretesAtivos()) {
            cancelar(contexto)
            return
        }

        val inicio = preferencias.lerInicioHora()
        val fim = preferencias.lerFimHora()
        val intervalo = preferencias.lerIntervaloHoras()
        if (!configuracaoLembreteValida(inicio, fim, intervalo)) return

        val agora = LocalDateTime.now()
        val proximo = proximoLembrete(agora, inicio, fim, intervalo)
        val atrasoMillis = Duration.between(agora, proximo).toMillis()

        val pedido = OneTimeWorkRequestBuilder<LembreteWorker>()
            .setInitialDelay(atrasoMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(contexto)
            .enqueueUniqueWork(NOME_DO_TRABALHO, ExistingWorkPolicy.REPLACE, pedido)
    }

    fun cancelar(contexto: Context) {
        WorkManager.getInstance(contexto).cancelUniqueWork(NOME_DO_TRABALHO)
    }
}