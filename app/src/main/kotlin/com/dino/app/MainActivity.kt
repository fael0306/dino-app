package com.dino.app

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.RenderProcessGoneDetail
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.webkit.JavaScriptReplyProxy
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max

/**
 * PaleoLab para Android.
 *
 * O app exibe o site PaleoLab (copiado em assets/www) num WebView, no "modo
 * aplicativo" do site (index.html?plataforma=app), e oferece à página os
 * recursos nativos que o WebView não tem: salvar arquivos, botão voltar,
 * cores das barras do sistema, vibração e seletor de arquivos.
 */
class MainActivity : ComponentActivity() {

    private companion object {
        const val TAG = "PaleoLab"
        const val NOME_PONTE = "PaleoLabNativo"
        const val ORIGEM = "https://${WebViewAssetLoader.DEFAULT_DOMAIN}"
        const val URL_INICIAL = "$ORIGEM/assets/www/index.html?plataforma=app"
        const val TEMPO_MAXIMO_ABERTURA_MS = 2500L
    }

    private lateinit var raiz: FrameLayout
    private lateinit var faixaStatus: View
    private lateinit var webView: WebView

    private val salvador by lazy { SalvadorArquivos(applicationContext) }
    private val tarefas: ExecutorService = Executors.newSingleThreadExecutor()

    private var paginaPronta = false
    private var callbackArquivo: ValueCallback<Array<Uri>>? = null

    /** Seletor de arquivos do sistema (botão "Importar arquivo" das conquistas). */
    private val seletorArquivo = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
        callbackArquivo?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultado.resultCode, resultado.data))
        callbackArquivo = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        val inicio = SystemClock.uptimeMillis()
        splash.setKeepOnScreenCondition {
            !paginaPronta && SystemClock.uptimeMillis() - inicio < TEMPO_MAXIMO_ABERTURA_MS
        }

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        construirLayout()
        configurarWebView()
        configurarVoltar()

        if (savedInstanceState == null || webView.restoreState(savedInstanceState) == null) {
            webView.loadUrl(URL_INICIAL)
        }
    }

    // ------------------------------------------------------------------
    // Layout: WebView entre as barras do sistema (edge-to-edge)
    // ------------------------------------------------------------------
    private fun construirLayout() {
        val corFundo = ContextCompat.getColor(this, R.color.fundo)
        val corSuperficie = ContextCompat.getColor(this, R.color.superficie)

        raiz = FrameLayout(this).apply { setBackgroundColor(corSuperficie) }
        webView = WebView(this).apply { setBackgroundColor(corFundo) }
        faixaStatus = View(this).apply { setBackgroundColor(corFundo) }

        raiz.addView(webView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        raiz.addView(faixaStatus, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, Gravity.TOP))
        setContentView(raiz)

        // Afasta o conteúdo da barra de status, da barra de navegação e do teclado
        ViewCompat.setOnApplyWindowInsetsListener(raiz) { _, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            val teclado = insets.getInsets(WindowInsetsCompat.Type.ime())
            (webView.layoutParams as FrameLayout.LayoutParams).setMargins(
                barras.left, barras.top, barras.right, max(barras.bottom, teclado.bottom)
            )
            webView.requestLayout()
            faixaStatus.layoutParams.height = barras.top
            faixaStatus.requestLayout()
            WindowInsetsCompat.CONSUMED
        }

        aplicarCoresSistema(escuro = !temaClaroDoSistema(), corTopo = corFundo, corBase = corSuperficie)
    }

    private fun temaClaroDoSistema(): Boolean =
        (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) !=
            android.content.res.Configuration.UI_MODE_NIGHT_YES

    /** Pinta as áreas das barras do sistema com as cores do tema da página. */
    private fun aplicarCoresSistema(escuro: Boolean, corTopo: Int, corBase: Int) {
        faixaStatus.setBackgroundColor(corTopo)
        raiz.setBackgroundColor(corBase)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = !escuro
            isAppearanceLightNavigationBars = !escuro
        }
    }

    // ------------------------------------------------------------------
    // WebView
    // ------------------------------------------------------------------
    @SuppressLint("SetJavaScriptEnabled")
    private fun configurarWebView() {
        val depuravel = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        WebView.setWebContentsDebuggingEnabled(depuravel)

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true          // conquistas, recordes e tema (localStorage)
            allowFileAccess = false
            allowContentAccess = false
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
            mediaPlaybackRequiresUserGesture = true
        }
        webView.overScrollMode = View.OVER_SCROLL_NEVER
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false

        // Serve assets/www em https://appassets.androidplatform.net/assets/www/
        // (origem segura: localStorage e canvas funcionam como num site normal)
        val carregadorAssets = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? =
                carregadorAssets.shouldInterceptRequest(request.url)

            /** Links externos (ex.: créditos do Ancient Earth) abrem no navegador. */
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (!request.isForMainFrame || request.url.host == WebViewAssetLoader.DEFAULT_DOMAIN) return false
                abrirNoNavegador(request.url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String?) {
                paginaPronta = true
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onRenderProcessGone(view: WebView, detail: RenderProcessGoneDetail): Boolean {
                // O processo do WebView caiu (pouca memória): recria a tela em vez de fechar o app
                Log.w(TAG, "Processo de renderização encerrado; recriando a atividade.")
                (view.parent as? ViewGroup)?.removeView(view)
                view.destroy()
                recreate()
                return true
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                view: WebView,
                callback: ValueCallback<Array<Uri>>,
                params: FileChooserParams
            ): Boolean {
                callbackArquivo?.onReceiveValue(null)
                callbackArquivo = callback
                return try {
                    seletorArquivo.launch(params.createIntent())
                    true
                } catch (e: ActivityNotFoundException) {
                    callbackArquivo = null
                    false
                }
            }
        }

        instalarPonte()
    }

    /**
     * Expõe window.PaleoLabNativo.postMessage(json) à página.
     * Com addWebMessageListener, a ponte só existe na página do app (origem
     * appassets) e nunca em iframes externos, como o globo do Ancient Earth.
     */
    private fun instalarPonte() {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
            WebViewCompat.addWebMessageListener(
                webView,
                NOME_PONTE,
                setOf(ORIGEM),
                object : WebViewCompat.WebMessageListener {
                    override fun onPostMessage(
                        view: WebView,
                        message: WebMessageCompat,
                        sourceOrigin: Uri,
                        isMainFrame: Boolean,
                        replyProxy: JavaScriptReplyProxy
                    ) {
                        if (isMainFrame) message.data?.let { tratarMensagem(it) }
                    }
                }
            )
        } else {
            // WebView muito antigo: mesma API, via interface JavaScript clássica
            webView.addJavascriptInterface(PonteLegada(), NOME_PONTE)
        }
    }

    private inner class PonteLegada {
        @JavascriptInterface
        fun postMessage(mensagem: String) {
            runOnUiThread { tratarMensagem(mensagem) }
        }
    }

    /** Mensagens enviadas pela página com chamarNativo(acao, dados). Roda na thread principal. */
    private fun tratarMensagem(json: String) {
        val msg = try {
            JSONObject(json)
        } catch (e: JSONException) {
            Log.w(TAG, "Mensagem inválida da página: $json")
            return
        }
        when (msg.optString("acao")) {
            "salvarArquivo" -> salvarArquivo(msg.optString("nome"), msg.optString("mime"), msg.optString("base64"))
            "temaMudou" -> aplicarCoresSistema(
                escuro = msg.optString("tema") == "escuro",
                corTopo = corOuPadrao(msg.optString("corTopo"), R.color.fundo),
                corBase = corOuPadrao(msg.optString("corBase"), R.color.superficie)
            )
            "vibrar" -> webView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            else -> Log.w(TAG, "Ação desconhecida: ${msg.optString("acao")}")
        }
    }

    private fun corOuPadrao(css: String, padrao: Int): Int =
        runCatching { Color.parseColor(css.trim()) }.getOrElse { ContextCompat.getColor(this, padrao) }

    // ------------------------------------------------------------------
    // Arquivos (relatório PDF e backup das conquistas)
    // ------------------------------------------------------------------
    private fun salvarArquivo(nome: String, mime: String, base64: String) {
        tarefas.execute {
            try {
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                val salvo = salvador.salvar(nome, mime, bytes)
                runOnUiThread { aoSalvarArquivo(salvo) }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao salvar $nome", e)
                runOnUiThread { Toast.makeText(this, R.string.erro_salvar, Toast.LENGTH_LONG).show() }
            }
        }
    }

    private fun aoSalvarArquivo(arquivo: ArquivoSalvo) {
        val texto = getString(
            if (arquivo.emDownloads) R.string.arquivo_salvo_downloads else R.string.arquivo_pronto,
            arquivo.nome
        )
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show()

        // PDF: oferece abrir num leitor. Outros (JSON): oferece compartilhar.
        val ehPdf = arquivo.mime == "application/pdf"
        val intencao: Intent = if (ehPdf) {
            Intent(Intent.ACTION_VIEW).setDataAndType(arquivo.uri, arquivo.mime)
        } else {
            Intent(Intent.ACTION_SEND).setType(arquivo.mime).putExtra(Intent.EXTRA_STREAM, arquivo.uri)
        }
        intencao.clipData = ClipData.newRawUri(arquivo.nome, arquivo.uri)
        intencao.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val titulo = getString(if (ehPdf) R.string.abrir_relatorio else R.string.compartilhar_arquivo)
        try {
            startActivity(Intent.createChooser(intencao, titulo))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.sem_app_para_abrir, Toast.LENGTH_LONG).show()
        }
    }

    private fun abrirNoNavegador(uri: Uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "Nenhum navegador para abrir $uri")
        }
    }

    // ------------------------------------------------------------------
    // Botão voltar: a página decide (fecha ficha, volta para a seção...)
    // ------------------------------------------------------------------
    private fun configurarVoltar() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val script = "(function(){try{return !!(window.PaleoLabApp&&window.PaleoLabApp.voltar());}catch(e){return false;}})()"
                webView.evaluateJavascript(script) { resultado ->
                    if (resultado != "true") finish()
                }
            }
        })
    }

    // ------------------------------------------------------------------
    // Ciclo de vida
    // ------------------------------------------------------------------
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        tarefas.shutdown()
        callbackArquivo?.onReceiveValue(null)
        callbackArquivo = null
        (webView.parent as? ViewGroup)?.removeView(webView)
        webView.destroy()
        super.onDestroy()
    }
}
