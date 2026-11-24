package com.devdenisviana.hidrataai

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val CANAL_LEMBRETES = "lembretes_agua"
private const val ID_NOTIFICACAO_LEMBRETE = 1

fun criarCanalDeLembretes(contexto: Context) {
    val canal = NotificationChannel(
        CANAL_LEMBRETES,
        "Lembretes de água",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Avisos para beber água ao longo do dia"
    }
    val gerenciador = contexto.getSystemService(NotificationManager::class.java)
    gerenciador.createNotificationChannel(canal)
}

@SuppressLint("MissingPermission")
fun mostrarLembrete(contexto: Context, faltamMl: Int) {
    val gerenciador = NotificationManagerCompat.from(contexto)
    if (!gerenciador.areNotificationsEnabled()) return
    criarCanalDeLembretes(contexto)

    val abrirApp = Intent(contexto, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    val intencao = PendingIntent.getActivity(
        contexto,
        0,
        abrirApp,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val texto = if (faltamMl > 0) {
        "Faltam $faltamMl ml para a sua meta de hoje."
    } else {
        "Você já bateu a meta de hoje!"
    }

    val notificacao = NotificationCompat.Builder(contexto, CANAL_LEMBRETES)
        .setSmallIcon(R.drawable.ic_notificacao)
        .setContentTitle("Hora de beber água!")
        .setContentText(texto)
        .setContentIntent(intencao)
        .setAutoCancel(true)
        .build()

    gerenciador.notify(ID_NOTIFICACAO_LEMBRETE, notificacao)
}