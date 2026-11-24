package com.devdenisviana.hidrataai

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {

    @Insert
    suspend fun inserir(registro: RegistroAgua)

    @Query(
        "SELECT * FROM registros " +
                "WHERE horaMillis >= :inicio AND horaMillis < :fim " +
                "ORDER BY horaMillis DESC, id DESC"
    )
    fun registrosDoPeriodo(inicio: Long, fim: Long): Flow<List<RegistroAgua>>

    @Query(
        "DELETE FROM registros WHERE id = " +
                "(SELECT id FROM registros ORDER BY horaMillis DESC, id DESC LIMIT 1)"
    )
    suspend fun desfazerUltimo()
}