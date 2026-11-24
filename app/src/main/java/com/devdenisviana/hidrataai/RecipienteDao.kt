package com.devdenisviana.hidrataai

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipienteDao {

    @Insert
    suspend fun inserir(recipiente: Recipiente)

    @Query("SELECT * FROM recipientes ORDER BY ml ASC, id ASC")
    fun todos(): Flow<List<Recipiente>>

    @Delete
    suspend fun apagar(recipiente: Recipiente)
}