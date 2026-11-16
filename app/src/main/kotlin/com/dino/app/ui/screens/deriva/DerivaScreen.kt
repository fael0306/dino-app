package com.dino.app.ui.screens.deriva

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dino.app.data.DinoData

@Composable
fun DerivaScreen() {
    var era by remember { mutableStateOf("66") }
    val opcoes = listOf("0" to "Mundo Atual", "66" to "Cretáceo Superior", "150" to "Jurássico Superior", "240" to "Triássico Médio")

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("🌍 Deriva Continental", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        var expanded by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(opcoes.first { it.first == era }.second) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                opcoes.forEach { (v, label) -> DropdownMenuItem(text = { Text(label) }, onClick = { era = v; expanded = false }) }
            }
        }
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().height(300.dp)) {
            AndroidView(factory = { ctx ->
                WebView(ctx).apply {
                    webViewClient = WebViewClient(); settings.javaScriptEnabled = true; settings.domStorageEnabled = true
                    loadUrl("https://dinosaurpictures.org/ancient-earth?_t=$era#$era")
                }
            }, update = { wv -> wv.loadUrl("https://dinosaurpictures.org/ancient-earth?_t=$era#$era") }, modifier = Modifier.fillMaxSize())
        }
        Spacer(Modifier.height(12.dp))
        Text("🗺️ Sítios Fósseis", style = MaterialTheme.typography.titleMedium)
        Box(Modifier.fillMaxWidth().height(300.dp)) {
            AndroidView(factory = { ctx ->
                WebView(ctx).apply {
                    webViewClient = WebViewClient(); settings.javaScriptEnabled = true
                    val markers = DinoData.COORDENADAS.entries.joinToString(",") { (nome, coords) ->
                        coords.joinToString(",") { "L.marker([${it.lat},${it.lon}]).addTo(map).bindPopup('${nome.replace("'", "\\'")}')" }
                    }
                    val html = """<html><head><meta name="viewport" content="width=device-width"><link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/><script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script><style>html,body,#map{height:100%;margin:0;}</style></head><body><div id="map"></div><script>var map=L.map('map').setView([0,0],2);L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png').addTo(map);$markers</script></body></html>"""
                    loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                }
            }, modifier = Modifier.fillMaxSize())
        }
    }
}