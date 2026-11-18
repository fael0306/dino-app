# 🦴 PaleoLab Científico – Android v2.0.0

Aplicativo Android do **PaleoLab Científico**, o laboratório virtual de paleontologia ([site](https://github.com/fael0306/dino)). O app traz as 12 seções do site v2.0.0 (Escala Real, Fósseis Reais, Deriva Continental, Linha do Tempo, Clima, Árvore Evolutiva, Extinção K‑Pg, Massa Corporal, Icnofósseis, Quiz & Batalha, Conquistas e Relatório PDF) com cara de aplicativo:

- barra superior com botão voltar e barra de navegação inferior (Início, Explorar, Laboratório, Desafios);
- fichas em *bottom sheet*, avisos em *snackbar*, botões grandes para toque e transições entre telas;
- botão **voltar do Android** integrado (fecha a ficha → volta para a seção → sai do app);
- **tema claro/escuro**, com as barras do sistema acompanhando a cor do app;
- relatório **PDF** e backup das conquistas salvos em **Downloads/PaleoLab**, com opção de abrir ou compartilhar;
- importação do backup pelo seletor de arquivos do Android;
- funciona **offline** (exceto o mapa e o globo da Terra antiga, que vêm da internet);
- ícone adaptativo e tela de abertura.

---

## 🧩 Como funciona

O app é uma "casca" nativa em Kotlin com um WebView que exibe o próprio site, guardado dentro do APK em `app/src/main/assets/www`. O site é aberto no **modo aplicativo** (`index.html?plataforma=app`), que troca o layout de site pelo layout de app.

Os dois projetos ficam em **repositórios separados**:

| Repositório | Conteúdo |
|-------------|----------|
| [fael0306/dino](https://github.com/fael0306/dino) | O site: é onde o conteúdo, as telas e o modo aplicativo são desenvolvidos |
| fael0306/dino-app (este) | O app Android: código nativo + uma cópia do site |

Assim, qualquer melhoria no site chega ao app copiando a pasta, sem reescrever telas em Kotlin.

### Atualizar o site dentro do app

```bash
./scripts/atualizar-site.sh            # baixa a versão atual do site no GitHub
./scripts/atualizar-site.sh ../dino    # ou usa uma cópia local do repositório do site
```

Depois é só fazer commit da pasta `app/src/main/assets/www` e, se for publicar, aumentar `versionCode`/`versionName` em `app/build.gradle`.

---

## 📦 Baixar o APK pronto (GitHub Actions)

A cada push na branch `main`, o GitHub compila o app automaticamente:

1. Abra a aba **Actions** do repositório e clique na execução mais recente de **Gerar APK**.
2. Em **Artifacts**, baixe **PaleoLab-apk** (um `.zip` com o `app-debug.apk`).
3. No celular, abra o APK e permita a instalação de fontes desconhecidas.

Também dá para rodar manualmente em **Actions → Gerar APK → Run workflow**, marcando "Baixar a versão mais nova do site" para compilar já com o site atualizado.

> O APK gerado é de depuração (assinado com chave de debug), ideal para testes e distribuição direta. Para publicar na Play Store é preciso configurar uma chave de assinatura própria.

---

## 🛠️ Compilar no Android Studio

**Pré‑requisitos:** Android Studio (Ladybug ou mais novo), JDK 17, Android SDK 35.

1. Clone o repositório e abra a pasta no Android Studio (**Open**).
2. (Opcional) Rode `./scripts/baixar-bibliotecas.sh` para incluir as bibliotecas de gráficos, mapa, árvore e PDF no app. Sem isso elas são baixadas da internet na primeira vez que forem usadas.
3. Clique em **Run ▶**.

Para depurar a página, abra `chrome://inspect` no Chrome do computador com o celular conectado (o WebView é inspecionável nas versões de depuração).

---

## 📁 Estrutura

```text
app/src/main/
├── kotlin/com/dino/app/
│   ├── MainActivity.kt      # WebView, ponte com a página, voltar, barras do sistema, seletor de arquivos
│   ├── Arquivos.kt          # Grava PDF/JSON em Downloads/PaleoLab e na pasta interna
│   └── NomesArquivo.kt      # Regras de nomes e tipos de arquivo (com testes)
├── assets/www/              # Cópia do site (gerada por scripts/atualizar-site.sh)
├── res/                     # Ícone, tela de abertura, cores e textos
└── AndroidManifest.xml
scripts/
├── atualizar-site.sh        # Copia o site para assets/www
└── baixar-bibliotecas.sh    # Baixa Chart.js, Leaflet, vis-network e jsPDF para uso offline
.github/workflows/android.yml  # Testes + APK a cada push
```

### Ponte entre o app e a página

- **Página → app:** o app injeta `window.PaleoLabNativo` apenas na página do PaleoLab (nunca em iframes externos, como o globo). A página envia mensagens JSON (`chamarNativo` em `js/ui.js` do site):
  - `salvarArquivo` – `nome`, `mime`, `base64`: grava o arquivo e oferece abrir (PDF) ou compartilhar (JSON);
  - `temaMudou` – `tema`, `corTopo`, `corBase`: pinta a barra de status e a de navegação;
  - `vibrar`: retorno tátil leve.
- **App → página:** o botão voltar chama `window.PaleoLabApp.voltar()`; se retornar `false`, o app fecha.

---

## 📄 Licença

Distribuído sob a licença MIT. As bibliotecas incluídas (Bootstrap Icons, Chart.js, Leaflet, vis-network, jsPDF) têm suas próprias licenças MIT/Apache.
