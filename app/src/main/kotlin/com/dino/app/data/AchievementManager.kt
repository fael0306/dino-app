package com.dino.app.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/** Ponto único para desbloquear conquistas; emite um evento só quando a conquista é nova. */
class AchievementManager(private val prefs: AppPreferences, private val scope: CoroutineScope) {
    private val _eventos = MutableSharedFlow<ConquistaInfo>(extraBufferCapacity = 16)
    val eventos: SharedFlow<ConquistaInfo> = _eventos.asSharedFlow()

    fun desbloquear(id: String) {
        scope.launch {
            if (prefs.desbloquear(id)) Conquistas.porId(id)?.let { _eventos.emit(it) }
        }
    }
}