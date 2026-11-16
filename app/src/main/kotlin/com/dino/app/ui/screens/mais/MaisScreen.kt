package com.dino.app.ui.screens.mais

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MaisScreen(nav: NavController) {
    val atalhos = listOf(
        Triple("deriva", "Deriva", "🌍"), Triple("extincao", "K-Pg", "☄️"),
        Triple("icnofosseis", "Icnofósseis", "👣"), Triple("massa", "Massa", "⚖️"),
        Triple("tempo", "Tempo", "⏳"), Triple("conquistas", "Conquistas", "🏆"),
        Triple("pdf", "PDF", "📄"), Triple("arvore", "Árvore", "🌳")
    )
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mais funcionalidades", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(atalhos) { (rota, titulo, emoji) ->
                Card(onClick = { nav.navigate(rota) }, modifier = Modifier.aspectRatio(1f)) {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(emoji, style = MaterialTheme.typography.displaySmall)
                        Spacer(Modifier.height(8.dp))
                        Text(titulo, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}