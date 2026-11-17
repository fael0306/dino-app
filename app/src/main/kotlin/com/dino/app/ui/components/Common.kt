package com.dino.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PaleoCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun SectionTitle(text: String, emoji: String = "") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (emoji.isNotEmpty()) { Text(emoji, style = MaterialTheme.typography.headlineSmall); Spacer(Modifier.width(8.dp)) }
        Text(text, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
fun DinoSilhueta(nome: String, alturaMetros: Double, modifier: Modifier = Modifier) {
    val cor = remember(nome) {
        val hue = (nome.hashCode() % 360 + 360) % 360
        Color.hsl(hue.toFloat(), 0.5f, 0.45f)
    }
    Box(
        modifier.heightIn(min = 80.dp).background(cor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)).padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🦕", style = MaterialTheme.typography.displaySmall)
            Text(nome, style = MaterialTheme.typography.labelMedium)
            Text("${"%.1f".format(alturaMetros)} m", style = MaterialTheme.typography.labelSmall)
        }
    }
}
