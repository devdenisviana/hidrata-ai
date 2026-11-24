package com.devdenisviana.hidrataai

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RegistroAgua::class, Recipiente::class],
    version = 1,
    exportSchema = false
)
abstract class BancoHidrata : RoomDatabase() {

    abstract fun registroDao(): RegistroDao

    abstract fun recipienteDao(): RecipienteDao

    companion object {
        @Volatile
        private var instancia: BancoHidrata? = null

        fun obter(contexto: Context): BancoHidrata {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    BancoHidrata::class.java,
                    "hidrata.db"
                ).build().also { instancia = it }
            }
        }
    }
}