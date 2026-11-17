package com.dino.app.ui.screens.deriva

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dino.app.data.DinoData

private fun Context.estaOnline(): Boolean = try {
    val cm = getSystemService(ConnectivityManager::class.java)
    cm.getNetworkCapabilities(cm.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
} catch (e: Exception) { true } // sem permissão/serviço: tenta carregar mesmo assim

private fun urlDeriva(era: String) = "https://dinosaurpictures.org/ancient-earth?_t=$era#$era"

private fun htmlMapa(): String {
    val markers = DinoData.COORDENADAS.entries.joinToString(";") { (nome, coords) ->
        coords.joinToString(";") { "L.marker([${it.lat},${it.lon}]).addTo(map).bindPopup('${nome.replace("'", "\\'")}')" }
    }
    return """<html><head><meta name="viewport" content="width=device-width"><link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/><script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script><style>html,body,#map{height:100%;margin:0;}</style></head><body><div id="map"></div><script>var map=L.map('map').setView([0,0],2);L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png').addTo(map);$markers</script></body></html>"""
}

@Composable
fun DerivaScreen() {
    val ctx = LocalContext.current
    var era by remember { mutableStateOf("66") }
    var tentativa by remember { mutableIntStateOf(0) }
    val online = remember(tentativa) { ctx.estaOnline() }
    var carregando by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf(false) }
    val opcoes = listOf("0" to "Mundo Atual", "66" to "Cretáceo Superior", "150" to "Jurássico Superior", "240" to "Triássico Médio")

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
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

        if (!online || erro) {
            Text("📡 Sem conexão: o mapa da Terra antiga e o mapa interativo precisam de internet.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { erro = false; carregando = true; tentativa++ }) { Text("Tentar novamente") }
        } else {
            if (carregando) LinearProgressIndicator(Modifier.fillMaxWidth())
            Box(Modifier.fillMaxWidth().height(300.dp)) {
                AndroidView(
                    factory = { c ->
                        WebView(c).apply {
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) { carregando = false }
                                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                                    if (request?.isForMainFrame == true) { erro = true; carregando = false }
                                }
                            }
                            settings.javaScriptEnabled = true; settings.domStorageEnabled = true
                            tag = urlDeriva(era); loadUrl(urlDeriva(era))
                        }
                    },
                    update = { wv ->
                        val alvo = urlDeriva(era)
                        if (wv.tag != alvo) { wv.tag = alvo; carregando = true; wv.loadUrl(alvo) }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("🗺️ Sítios Fósseis", style = MaterialTheme.typography.titleMedium)
        if (online) {
            Box(Modifier.fillMaxWidth().height(300.dp)) {
                AndroidView(factory = { c ->
                    WebView(c).apply {
                        webViewClient = WebViewClient(); settings.javaScriptEnabled = true
                        loadDataWithBaseURL(null, htmlMapa(), "text/html", "UTF-8", null)
                    }
                }, modifier = Modifier.fillMaxSize())
            }
        } else {
            // Fallback offline: coordenadas guardadas no app
            DinoData.COORDENADAS.forEach { (nome, coords) ->
                coords.forEach { Text("• $nome — lat ${it.lat}, lon ${it.lon}", style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}