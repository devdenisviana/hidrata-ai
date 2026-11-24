package com.devdenisviana.hidrataai

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat

@Composable
fun SecaoLembretes(viewModel: LembretesViewModel) {
    val contexto = LocalContext.current
    val ativos by viewModel.ativos.collectAsState()
    val intervalo by viewModel.intervaloHoras.collectAsState()
    val inicio by viewModel.inicioHora.collectAsState()
    val fim by viewModel.fimHora.collectAsState()

    var notificacoesLigadas by remember {
        mutableStateOf(NotificationManagerCompat.from(contexto).areNotificationsEnabled())
    }

    val pedirPermissao = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        notificacoesLigadas = NotificationManagerCompat.from(contexto).areNotificationsEnabled()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Lembretes", style = MaterialTheme.typography.titleMedium)
            Switch(
                checked = ativos,
                onCheckedChange = { ligar ->
                    if (ligar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        pedirPermissao.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    viewModel.definirAtivos(ligar)
                }
            )
        }
        Text(
            text = "Avisos para beber água dentro do horário escolhido. " +
                    "Eles param quando você bate a meta do dia.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!notificacoesLigadas) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "As notificações do Hidrata Aí estão desativadas no celular. " +
                        "Ative em Configurações > Apps > Hidrata Aí > Notificações.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (ativos) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Intervalo entre os avisos",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                INTERVALOS_LEMBRETE_HORAS.forEach { horas ->
                    FilterChip(
                        selected = intervalo == horas,
                        onClick = { viewModel.definirIntervalo(horas) },
                        label = { Text("$horas h") }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            LinhaDeHora(
                rotulo = "Início",
                hora = inicio,
                aoDiminuir = { viewModel.definirInicio(inicio - 1) },
                aoAumentar = { viewModel.definirInicio(inicio + 1) }
            )
            LinhaDeHora(
                rotulo = "Fim",
                hora = fim,
                aoDiminuir = { viewModel.definirFim(fim - 1) },
                aoAumentar = { viewModel.definirFim(fim + 1) }
            )

            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { viewModel.enviarTeste() }) {
                Text("Enviar lembrete de teste")
            }
        }
    }
}

@Composable
private fun LinhaDeHora(
    rotulo: String,
    hora: Int,
    aoDiminuir: () -> Unit,
    aoAumentar: () -> Unit
) {
    val horaTexto = hora.toString().padStart(2, '0')
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$rotulo: ${horaTexto}h", style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = aoDiminuir) { Text("−") }
            OutlinedButton(onClick = aoAumentar) { Text("+") }
        }
    }
}