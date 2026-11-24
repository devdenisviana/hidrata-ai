package com.devdenisviana.hidrataai

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros")
data class RegistroAgua(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val horaMillis: Long,
    val ml: Int
)