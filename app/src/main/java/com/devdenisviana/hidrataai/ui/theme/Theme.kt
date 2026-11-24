package com.devdenisviana.hidrataai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaClaro = lightColorScheme(
    primary = AzulAgua,
    onPrimary = Superficie,
    primaryContainer = AzulSuave,
    onPrimaryContainer = TextoPrincipal,
    secondary = AzulClaro,
    onSecondary = TextoPrincipal,
    background = Fundo,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    surfaceVariant = AzulSuave,
    onSurfaceVariant = TextoSecundario,
    outline = Contorno
)

@Composable
fun HidrataAiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}