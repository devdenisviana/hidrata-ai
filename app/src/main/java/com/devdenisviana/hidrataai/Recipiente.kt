package com.devdenisviana.hidrataai

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipientes")
data class Recipiente(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val ml: Int
)

enum class TipoRecipiente { COPO, GARRAFA }

const val LIMITE_COPO_ML = 350
const val ID_RECIPIENTE_INICIAL = -1L

fun tipoDoRecipiente(ml: Int): TipoRecipiente {
    return if (ml <= LIMITE_COPO_ML) TipoRecipiente.COPO else TipoRecipiente.GARRAFA
}

val RECIPIENTES_PADRAO = listOf(
    Recipiente(id = -1, nome = "Copo", ml = 200),
    Recipiente(id = -2, nome = "Copo grande", ml = 300),
    Recipiente(id = -3, nome = "Garrafinha", ml = 500),
    Recipiente(id = -4, nome = "Garrafa", ml = 750),
    Recipiente(id = -5, nome = "Garrafa grande", ml = 1000)
)