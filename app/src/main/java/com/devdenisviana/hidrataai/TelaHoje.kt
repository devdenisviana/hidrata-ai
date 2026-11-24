package com.devdenisviana.hidrataai

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TelaHoje(viewModel: HidrataViewModel, modifier: Modifier = Modifier) {
    val total by viewModel.totalDeHojeMl.collectAsState()
    val registros by viewModel.registrosDeHoje.collectAsState()
    val recipientes by viewModel.recipientes.collectAsState()
    val selecionado by viewModel.recipienteSelecionado.collectAsState()

    val meta = META_PADRAO_ML
    val faltam = restanteMl(total, meta)
    val progresso by animateFloatAsState(
        targetValue = fracaoDaMeta(total, meta),
        label = "progresso"
    )
    val formatoHora = remember { DateTimeFormatter.ofPattern("HH:mm") }

    var mostrarOutroValor by rememberSaveable { mutableStateOf(false) }
    var textoOutroValor by rememberSaveable { mutableStateOf("") }

    if (mostrarOutroValor) {
        val ml = textoOutroValor.toIntOrNull() ?: 0
        AlertDialog(
            onDismissRequest = {
                mostrarOutroValor = false
                textoOutroValor = ""
            },
            title = { Text("Outro valor") },
            text = {
                OutlinedTextField(
                    value = textoOutroValor,
                    onValueChange = { novo ->
                        if (novo.length <= 4 && novo.all { it.isDigit() }) {
                            textoOutroValor = novo
                        }
                    },
                    label = { Text("Quantidade (ml)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                TextButton(
                    enabled = registroValido(ml),
                    onClick = {
                        viewModel.beberOutroValor(ml)
                        textoOutroValor = ""
                        mostrarOutroValor = false
                    }
                ) {
                    Text("Beber")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarOutroValor = false
                        textoOutroValor = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

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
                text = "Recipiente",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recipientes, key = { it.id }) { recipiente ->
                    CartaoRecipiente(
                        recipiente = recipiente,
                        selecionado = recipiente.id == selecionado.id,
                        aoTocar = { viewModel.escolherRecipiente(recipiente.id) }
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
                Text("Beber ${selecionado.ml} ml")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { mostrarOutroValor = true }) {
                    Text("Outro valor")
                }
                TextButton(
                    onClick = { viewModel.desfazerUltimo() },
                    enabled = registros.isNotEmpty()
                ) {
                    Text("Desfazer último")
                }
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
                        Text(
                            text = "${registro.ml} ml",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
private fun CartaoRecipiente(
    recipiente: Recipiente,
    selecionado: Boolean,
    aoTocar: () -> Unit
) {
    val corFundo = if (selecionado) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val corBorda = if (selecionado) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    Surface(
        onClick = aoTocar,
        shape = RoundedCornerShape(16.dp),
        color = corFundo,
        border = BorderStroke(if (selecionado) 2.dp else 1.dp, corBorda),
        modifier = Modifier.width(104.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconeRecipiente(
                tipo = tipoDoRecipiente(recipiente.ml),
                cor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = recipiente.nome,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${recipiente.ml} ml",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}