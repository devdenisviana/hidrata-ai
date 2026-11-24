package com.devdenisviana.hidrataai

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap

enum class Aba(val rotulo: String) {
    HOJE("Hoje"),
    HISTORICO("Histórico"),
    AJUSTES("Ajustes")
}

@Composable
fun IconeAba(aba: Aba, cor: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val largura = size.width
        val altura = size.height

        when (aba) {
            Aba.HOJE -> {
                val gota = Path().apply {
                    moveTo(largura * 0.5f, altura * 0.06f)
                    cubicTo(
                        largura * 0.5f, altura * 0.06f,
                        largura * 0.82f, altura * 0.42f,
                        largura * 0.82f, altura * 0.62f
                    )
                    cubicTo(
                        largura * 0.82f, altura * 0.82f,
                        largura * 0.68f, altura * 0.94f,
                        largura * 0.5f, altura * 0.94f
                    )
                    cubicTo(
                        largura * 0.32f, altura * 0.94f,
                        largura * 0.18f, altura * 0.82f,
                        largura * 0.18f, altura * 0.62f
                    )
                    cubicTo(
                        largura * 0.18f, altura * 0.42f,
                        largura * 0.5f, altura * 0.06f,
                        largura * 0.5f, altura * 0.06f
                    )
                    close()
                }
                drawPath(path = gota, color = cor)
            }

            Aba.HISTORICO -> {
                val raio = CornerRadius(largura * 0.04f, largura * 0.04f)
                val larguraBarra = largura * 0.2f
                listOf(0.12f to 0.45f, 0.40f to 0.75f, 0.68f to 0.6f).forEach { (x, alturaBarra) ->
                    drawRoundRect(
                        color = cor,
                        topLeft = Offset(largura * x, altura * (0.92f - alturaBarra)),
                        size = Size(larguraBarra, altura * alturaBarra),
                        cornerRadius = raio
                    )
                }
            }

            Aba.AJUSTES -> {
                listOf(0.22f to 0.65f, 0.5f to 0.3f, 0.78f to 0.7f).forEach { (y, xBotao) ->
                    drawLine(
                        color = cor,
                        start = Offset(largura * 0.1f, altura * y),
                        end = Offset(largura * 0.9f, altura * y),
                        strokeWidth = largura * 0.09f,
                        cap = StrokeCap.Round
                    )
                    drawCircle(
                        color = cor,
                        radius = largura * 0.13f,
                        center = Offset(largura * xBotao, altura * y)
                    )
                }
            }
        }
    }
}

@Composable
fun TelaEmBreve(titulo: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "$titulo: em breve",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}