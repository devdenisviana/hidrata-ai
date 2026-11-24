package com.devdenisviana.hidrataai

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devdenisviana.hidrataai.ui.theme.HidrataAiTheme

class MainActivity : ComponentActivity() {

    private val viewModel: HidrataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            HidrataAiTheme {
                var abaAtual by rememberSaveable { mutableStateOf(Aba.HOJE) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            Aba.entries.forEach { aba ->
                                NavigationBarItem(
                                    selected = abaAtual == aba,
                                    onClick = { abaAtual = aba },
                                    icon = {
                                        IconeAba(
                                            aba = aba,
                                            cor = LocalContentColor.current,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = { Text(aba.rotulo) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    val conteudo = Modifier.padding(innerPadding)
                    when (abaAtual) {
                        Aba.HOJE -> TelaHoje(viewModel = viewModel, modifier = conteudo)
                        Aba.HISTORICO -> TelaEmBreve(titulo = "Histórico", modifier = conteudo)
                        Aba.AJUSTES -> TelaEmBreve(titulo = "Ajustes", modifier = conteudo)
                    }
                }
            }
        }
    }
}