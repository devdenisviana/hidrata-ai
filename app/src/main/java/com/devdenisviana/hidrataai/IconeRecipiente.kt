package com.devdenisviana.hidrataai

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun IconeRecipiente(tipo: TipoRecipiente, cor: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val largura = size.width
        val altura = size.height
        val traco = Stroke(width = largura * 0.07f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        val corAgua = cor.copy(alpha = 0.35f)

        when (tipo) {
            TipoRecipiente.COPO -> {
                val contorno = Path().apply {
                    moveTo(largura * 0.20f, altura * 0.12f)
                    lineTo(largura * 0.80f, altura * 0.12f)
                    lineTo(largura * 0.70f, altura * 0.90f)
                    lineTo(largura * 0.30f, altura * 0.90f)
                    close()
                }
                val agua = Path().apply {
                    moveTo(largura * 0.2423f, altura * 0.45f)
                    lineTo(largura * 0.7577f, altura * 0.45f)
                    lineTo(largura * 0.70f, altura * 0.90f)
                    lineTo(largura * 0.30f, altura * 0.90f)
                    close()
                }
                drawPath(path = agua, color = corAgua)
                drawPath(path = contorno, color = cor, style = traco)
            }

            TipoRecipiente.GARRAFA -> {
                val contorno = Path().apply {
                    moveTo(largura * 0.42f, altura * 0.06f)
                    lineTo(largura * 0.58f, altura * 0.06f)
                    lineTo(largura * 0.58f, altura * 0.26f)
                    lineTo(largura * 0.72f, altura * 0.38f)
                    lineTo(largura * 0.72f, altura * 0.92f)
                    lineTo(largura * 0.28f, altura * 0.92f)
                    lineTo(largura * 0.28f, altura * 0.38f)
                    lineTo(largura * 0.42f, altura * 0.26f)
                    close()
                }
                val agua = Path().apply {
                    moveTo(largura * 0.28f, altura * 0.58f)
                    lineTo(largura * 0.72f, altura * 0.58f)
                    lineTo(largura * 0.72f, altura * 0.92f)
                    lineTo(largura * 0.28f, altura * 0.92f)
                    close()
                }
                drawPath(path = agua, color = corAgua)
                drawPath(path = contorno, color = cor, style = traco)
            }
        }
    }
}