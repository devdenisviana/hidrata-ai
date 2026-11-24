# Hidrata Aí

Aplicativo Android para acompanhar o consumo diário de água. Você escolhe o recipiente (copo ou garrafa), toca em **Beber** e acompanha o progresso da meta do dia, o histórico da semana e lembretes ao longo do dia.

Projeto de portfólio, feito do zero em **Kotlin** com **Jetpack Compose**.

## Funcionalidades

- **Meta diária** personalizável, com sugestão opcional pelo peso (35 ml por kg).
- **Recipientes**: copos e garrafas prontos, e a possibilidade de criar os seus (nome e tamanho).
- **Registro rápido**: escolha o recipiente e toque em Beber, ou digite outro valor. Também é possível desfazer o último registro.
- **Histórico** dos últimos 7 dias, com gráfico de barras, média diária e dias em que a meta foi batida.
- **Lembretes** com intervalo (1 a 4 horas) e janela de horário configuráveis. Eles param quando a meta do dia é atingida.
- **Funciona offline** e sem conta. Os dados ficam só no aparelho.

## Tecnologias

- Kotlin e Jetpack Compose (Material 3)
- ViewModel e StateFlow
- Room (banco de dados local)
- WorkManager (lembretes agendados) e notificações
- Gráficos e ícones desenhados com `Canvas`, sem bibliotecas externas
- Testes unitários com JUnit

## Como o código está organizado

A lógica de negócio fica em funções puras, sem dependência do Android, e é coberta por testes unitários:

| Arquivo | Responsabilidade |
|---|---|
| `Agua.kt` | meta sugerida pelo peso, fração da meta, quanto falta |
| `Tempo.kt` | início do dia e tempo até a virada do dia |
| `Historico.kt` | totais por dia, média e dias que bateram a meta |
| `Lembretes.kt` | horários dos lembretes e regra de quando avisar |
| `Validacao.kt` | validação de valores digitados |
| `Recipiente.kt` | tipos e tamanhos de recipientes |

As telas (`TelaHoje`, `TelaHistorico`, `TelaAjustes`) e os ViewModels ficam separados da lógica.

## Como rodar

Requisitos: Android Studio recente e um emulador ou celular com Android 8.0 (API 26) ou mais novo.

1. Clone o repositório.
2. Abra a pasta no Android Studio e aguarde a sincronização do Gradle.
3. Execute o app (botão ▶).

Para rodar os testes unitários:

```
./gradlew test
```

No Windows, use `gradlew.bat test`.

## Privacidade

O app não coleta nem envia dados. Veja a [Política de Privacidade](PRIVACY.md).

## Contato

agua@denisviana.com.br