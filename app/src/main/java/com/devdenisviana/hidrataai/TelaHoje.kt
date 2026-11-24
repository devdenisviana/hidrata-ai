package com.devdenisviana.hidrataai

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TelaHoje(viewModel: HidrataViewModel, modifier: Modifier = Modifier) {
    val total by viewModel.totalDeHojeMl.collectAsState()
    val registros by viewModel.registrosDeHoje.collectAsState()
    val copo by viewModel.copoMl.collectAsState()

    val meta = META_PADRAO_ML
    val faltam = restanteMl(total, meta)
    val progresso by animateFloatAsState(
        targetValue = fracaoDaMeta(total, meta),
        label = "progresso"
    )
    val formatoHora = remember { DateTimeFormatter.ofPattern("HH:mm") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Hidrata Aí",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(24.dp))
        }

        item {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progresso },
                    modifier = Modifier.size(240.dp),
                    strokeWidth = 20.dp,
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$total",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "de $meta ml",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text(
                text = if (faltam > 0) "Faltam $faltam ml" else "Meta do dia atingida!",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Tamanho do copo (ml)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(100, 200, 300, 400).forEach { ml ->
                    FilterChip(
                        selected = copo == ml,
                        onClick = { viewModel.escolherCopo(ml) },
                        label = { Text("$ml") }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = { viewModel.beber() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Beber $copo ml")
            }
            TextButton(
                onClick = { viewModel.desfazerUltimo() },
                enabled = registros.isNotEmpty()
            ) {
                Text("Desfazer último")
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Registros de hoje",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        if (registros.isEmpty()) {
            item {
                Text(
                    text = "Nenhum registro ainda. Toque em Beber!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            items(registros, key = { it.id }) { registro ->
                val hora = Instant.ofEpochMilli(registro.horaMillis)
                    .atZone(ZoneId.systemDefault())
                    .format(formatoHora)
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = hora, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "${registro.ml} ml", style = MaterialTheme.typography.bodyLarge)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}