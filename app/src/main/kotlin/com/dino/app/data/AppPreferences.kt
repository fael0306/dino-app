package com.dino.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dino.app.data.model.ResultadoQuiz
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "paleolab")

class AppPreferences(private val context: Context) {
    private val CONQUISTAS = stringSetPreferencesKey("conquistas")
    private val PERIODOS = stringSetPreferencesKey("periodos_visitados")
    private val HISTORICO = stringPreferencesKey("historico_quiz")

    val conquistasFlow: Flow<Set<String>> = context.dataStore.data.map { it[CONQUISTAS] ?: emptySet() }
    val periodosFlow: Flow<Set<String>> = context.dataStore.data.map { it[PERIODOS] ?: emptySet() }
    val historicoFlow: Flow<List<ResultadoQuiz>> = context.dataStore.data.map { ResultadoQuiz.parseLista(it[HISTORICO]) }

    /** Retorna true somente se a conquista era nova. */
    suspend fun desbloquear(id: String): Boolean {
        var nova = false
        context.dataStore.edit { prefs ->
            val atual = prefs[CONQUISTAS] ?: emptySet()
            if (id !in atual) { nova = true; prefs[CONQUISTAS] = atual + id }
        }
        return nova
    }

    /** Registra o período e devolve o conjunto atualizado de períodos visitados. */
    suspend fun visitarPeriodo(nome: String): Set<String> {
        var resultado: Set<String> = emptySet()
        context.dataStore.edit { prefs ->
            val novo = (prefs[PERIODOS] ?: emptySet()) + nome
            prefs[PERIODOS] = novo
            resultado = novo
        }
        return resultado
    }

    suspend fun registrarQuiz(r: ResultadoQuiz) {
        context.dataStore.edit { prefs ->
            val lista = (ResultadoQuiz.parseLista(prefs[HISTORICO]) + r).takeLast(50)
            prefs[HISTORICO] = lista.joinToString("\n") { it.serializar() }
        }
    }
}
