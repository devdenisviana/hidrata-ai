package com.devdenisviana.hidrataai

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TelaHistorico(
    viewModel: HistoricoViewModel,
    metaMl: Int,
    modifier: Modifier = Modifier
) {
    val totais by viewModel.ultimosSeteDias.collectAsState()

    val corAtingiu = MaterialTheme.colorScheme.primary
    val corNormal = MaterialTheme.colorScheme.secondary
    val corMeta = MaterialTheme.colorScheme.outline
    val maximo = maxOf(metaMl, totais.maxOfOrNull { it.ml } ?: 0, 1)

    val formatoDia = remember { DateTimeFormatter.ofPattern("dd/MM") }
    val idioma = remember { Locale.forLanguageTag("pt-BR") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Histórico",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Últimos 7 dias (linha tracejada = sua meta)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            if (totais.isEmpty()) return@Canvas

            val faixa = size.width / totais.size
            val larguraBarra = faixa * 0.6f

            totais.forEachIndexed { indice, total ->
                val alturaBarra = size.height * (total.ml.toFloat() / maximo)
                drawRoundRect(
                    color = if (total.ml >= metaMl) corAtingiu else corNormal,
                    topLeft = Offset(
                        x = faixa * indice + (faixa - larguraBarra) / 2,
                        y = size.height - alturaBarra
                    ),
                    size = Size(larguraBarra, alturaBarra),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }

            val yMeta = size.height * (1 - metaMl.toFloat() / maximo)
            drawLine(
                color = corMeta,
                start = Offset(0f, yMeta),
                end = Offset(size.width, yMeta),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 16f))
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            totais.forEach { total ->
                Text(
                    text = total.dia.dayOfWeek.getDisplayName(TextStyle.SHORT, idioma),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = "Média: ${mediaDiaria(totais)} ml por dia",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Meta atingida em ${diasQueBateramMeta(totais, metaMl)} de ${totais.size} dias",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(24.dp))
        Text(text = "Dia a dia", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        totais.asReversed().forEach { total ->
            val nomeDoDia = total.dia.dayOfWeek.getDisplayName(TextStyle.SHORT, idioma)
            val atingiu = total.ml >= metaMl
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${total.dia.format(formatoDia)} · $nomeDoDia",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (atingiu) "${total.ml} ml ✓" else "${total.ml} ml",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (atingiu) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        }
    }
}