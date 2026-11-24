package com.devdenisviana.hidrataai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TelaAjustes(viewModel: HidrataViewModel, modifier: Modifier = Modifier) {
    val metaAtual by viewModel.metaMl.collectAsState()
    val pesoAtual by viewModel.pesoKg.collectAsState()
    val recipientes by viewModel.recipientes.collectAsState()
    val personalizados = recipientes.filter { it.id > 0 }

    var textoMeta by rememberSaveable { mutableStateOf(metaAtual.toString()) }
    var textoPeso by rememberSaveable {
        mutableStateOf(if (pesoAtual > 0) pesoAtual.toString() else "")
    }
    var textoNome by rememberSaveable { mutableStateOf("") }
    var textoMl by rememberSaveable { mutableStateOf("") }

    val metaDigitada = textoMeta.toIntOrNull() ?: 0
    val pesoDigitado = textoPeso.toIntOrNull() ?: 0
    val mlDigitado = textoMl.toIntOrNull() ?: 0

    val teclaNumerica = KeyboardOptions(keyboardType = KeyboardType.Number)

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Ajustes",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(24.dp))

        Text(text = "Meta diária", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = textoMeta,
            onValueChange = { novo ->
                if (novo.length <= 5 && novo.all { it.isDigit() }) textoMeta = novo
            },
            label = { Text("Meta (ml)") },
            singleLine = true,
            keyboardOptions = teclaNumerica,
            isError = textoMeta.isNotEmpty() && !metaValida(metaDigitada),
            supportingText = { Text("Entre $META_MIN_ML e $META_MAX_ML ml") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.definirMeta(metaDigitada) },
            enabled = metaValida(metaDigitada) && metaDigitada != metaAtual
        ) {
            Text("Salvar meta")
        }

        Spacer(Modifier.height(32.dp))
        Text(text = "Sugestão pelo peso", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Usamos 35 ml por kg como referência. É só uma sugestão: ajuste do seu jeito. " +
                    "Para orientação de saúde, procure um profissional.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = textoPeso,
            onValueChange = { novo ->
                if (novo.length <= 3 && novo.all { it.isDigit() }) textoPeso = novo
            },
            label = { Text("Peso (kg)") },
            singleLine = true,
            keyboardOptions = teclaNumerica,
            isError = textoPeso.isNotEmpty() && !pesoValido(pesoDigitado),
            supportingText = { Text("Entre $PESO_MIN_KG e $PESO_MAX_KG kg") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.definirPeso(pesoDigitado)
                textoMeta = metaSugeridaPorPeso(pesoDigitado).toString()
            },
            enabled = pesoValido(pesoDigitado)
        ) {
            Text("Sugerir meta")
        }

        Spacer(Modifier.height(32.dp))
        Text(text = "Meus recipientes", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (personalizados.isEmpty()) {
            Text(
                text = "Você ainda não criou nenhum. Os tamanhos prontos aparecem na tela Hoje.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            personalizados.forEach { recipiente ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconeRecipiente(
                        tipo = tipoDoRecipiente(recipiente.ml),
                        cor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = recipiente.nome, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "${recipiente.ml} ml",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = { viewModel.apagarRecipiente(recipiente) }) {
                        Text("Apagar")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(text = "Novo recipiente", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = textoNome,
            onValueChange = { novo ->
                if (novo.length <= MAX_NOME_RECIPIENTE) textoNome = novo
            },
            label = { Text("Nome (ex.: Minha garrafa)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = textoMl,
            onValueChange = { novo ->
                if (novo.length <= 4 && novo.all { it.isDigit() }) textoMl = novo
            },
            label = { Text("Tamanho (ml)") },
            singleLine = true,
            keyboardOptions = teclaNumerica,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.criarRecipiente(textoNome, mlDigitado)
                textoNome = ""
                textoMl = ""
            },
            enabled = recipienteValido(textoNome, mlDigitado)
        ) {
            Text("Adicionar recipiente")
        }
        Spacer(Modifier.height(24.dp))
    }
}