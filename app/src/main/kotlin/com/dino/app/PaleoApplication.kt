package com.dino.app

import android.app.Application
import com.dino.app.data.AppPreferences

class PaleoApplication : Application() {
    lateinit var prefs: AppPreferences

    override fun onCreate() {
        super.onCreate()
        // Inicializa o gerenciador de preferências (conquistas e locais visitados)
        prefs = AppPreferences(this)
    }
}