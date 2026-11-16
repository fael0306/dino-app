package com.dino.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "paleolab")

class AppPreferences(private val context: Context) {
    private val CONQUISTAS = stringSetPreferencesKey("conquistas")
    private val PERIODOS = stringSetPreferencesKey("periodos_visitados")

    val conquistasFlow: Flow<Set<String>> = context.dataStore.data.map { it[CONQUISTAS] ?: emptySet() }
    val periodosFlow: Flow<Set<String>> = context.dataStore.data.map { it[PERIODOS] ?: emptySet() }

    suspend fun desbloquear(id: String) {
        context.dataStore.edit { it[CONQUISTAS] = (it[CONQUISTAS] ?: emptySet()) + id }
    }

    suspend fun visitarPeriodo(nome: String) {
        context.dataStore.edit { it[PERIODOS] = (it[PERIODOS] ?: emptySet()) + nome }
    }
}