package com.dino.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dino.app.data.AchievementManager
import com.dino.app.data.ConquistaInfo
import kotlinx.coroutines.delay

/** Banner animado que aparece no topo quando uma conquista é desbloqueada. */
@Composable
fun AchievementHost(manager: AchievementManager, modifier: Modifier = Modifier) {
    var visivel by remember { mutableStateOf(false) }
    var ultima by remember { mutableStateOf<ConquistaInfo?>(null) }

    LaunchedEffect(manager) {
        manager.eventos.collect { c ->
            ultima = c; visivel = true
            delay(3000)
            visivel = false
            delay(400)
        }
    }

    AnimatedVisibility(
        visible = visivel, modifier = modifier,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut()
    ) {
        ultima?.let { c ->
            Card(
                Modifier.padding(12.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary, contentColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(c.emoji, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Conquista desbloqueada!", style = MaterialTheme.typography.labelMedium)
                        Text(c.nome, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}