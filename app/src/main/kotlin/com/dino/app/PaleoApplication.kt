package com.dino.app

import android.app.Application
import android.content.Context
import com.dino.app.data.AchievementManager
import com.dino.app.data.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class PaleoApplication : Application() {
    lateinit var prefs: AppPreferences
    lateinit var achievements: AchievementManager
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        prefs = AppPreferences(this)
        achievements = AchievementManager(prefs, appScope)
    }
}

val Context.paleoApp: PaleoApplication get() = applicationContext as PaleoApplication
