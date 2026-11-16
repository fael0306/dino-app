package com.dino.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.dino.app.ui.screens.arvore.ArvoreScreen
import com.dino.app.ui.screens.clima.ClimaScreen
import com.dino.app.ui.screens.conquistas.ConquistasScreen
import com.dino.app.ui.screens.deriva.DerivaScreen
import com.dino.app.ui.screens.escala.EscalaScreen
import com.dino.app.ui.screens.extincao.ExtincaoScreen
import com.dino.app.ui.screens.fosseis.FosseisScreen
import com.dino.app.ui.screens.icnofosseis.IcnofosseisScreen
import com.dino.app.ui.screens.mais.MaisScreen
import com.dino.app.ui.screens.massa.MassaScreen
import com.dino.app.ui.screens.pdf.PdfScreen
import com.dino.app.ui.screens.quiz.QuizScreen
import com.dino.app.ui.screens.tempo.TempoScreen

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val primaryTabs = listOf(
    Tab("escala", "Escala", Icons.Default.Straighten),
    Tab("fosseis", "Fósseis", Icons.Default.Pets),
    Tab("quiz", "Quiz", Icons.Default.Quiz),
    Tab("clima", "Clima", Icons.Default.Cloud),
    Tab("mais", "Mais", Icons.Default.Menu)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val currentDest = backStack?.destination

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PaleoLab 🦴") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                primaryTabs.forEach { tab ->
                    val selected = currentDest?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            nav.navigate(tab.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, tab.label) }, 
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        // Aqui ele liga cada rota à sua respectiva tela
        NavHost(navController = nav, startDestination = "escala", modifier = Modifier.padding(padding)) {
            composable("escala") { EscalaScreen() }
            composable("fosseis") { FosseisScreen() }
            composable("quiz") { QuizScreen() }
            composable("clima") { ClimaScreen() }
            composable("mais") { MaisScreen(nav) }
            
            // Telas acessadas através do botão "Mais"
            composable("deriva") { DerivaScreen() }
            composable("extincao") { ExtincaoScreen() }
            composable("icnofosseis") { IcnofosseisScreen() }
            composable("massa") { MassaScreen() }
            composable("tempo") { TempoScreen() }
            composable("conquistas") { ConquistasScreen() }
            composable("pdf") { PdfScreen() }
            composable("arvore") { ArvoreScreen() }
        }
    }
}