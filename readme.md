# 🦴 PaleoLab Científico - Android (Kotlin) v1.0.0

Este é o aplicativo oficial para Android do **PaleoLab**, um laboratório virtual interativo para o ensino de paleontologia e ciências naturais. 

Esta versão é um porte nativo da aplicação web (v1.6.0) para a plataforma Android, utilizando **Kotlin** e **Jetpack Compose**. O app foi reescrito do zero para oferecer uma experiência fluida, suporte offline para os dados principais e integração total com as APIs nativas do sistema.

---

## ✨ Funcionalidades (12 Módulos)

O aplicativo conta com 12 abas/telas interativas:

1. **📏 Escala Real**: Compare até 3 animais (dinossauros, humano ou elefante) lado a lado com silhuetas proporcionais.
2. **🗺️ Deriva Continental**: Globo interativo da Terra antiga via WebView e mapa de sítios fósseis com Leaflet.
3. **🦠 Extinção K‑Pg**: Simulador do impacto do asteroide baseado no modelo matemático Lotka‑Volterra (Runge‑Kutta 4) com gráficos em Canvas.
4. **👣 Icnofósseis**: Jogo "Paleo‑Detetive" onde o usuário identifica icnogéneros a partir de características da pegada.
5. **🦴 Fósseis Reais**: Museu com espécies reais, imagens e curiosidades taxonómicas.
6. **⚖️ Massa Corporal**: Estimativa de massa pela circunferência do fêmur (Campione & Evans, 2012) com gráfico de dispersão comparativo.
7. **📝 Quiz**: Dois modos de jogo:
   - **Normal**: 20 perguntas em 3 níveis (Fácil, Médio, Difícil) com revisão completa.
   - **⚔️ Batalha**: Compare dois dinossauros em 5 perguntas dinâmicas geradas aleatoriamente!
8. **⏳ Linha do Tempo**: Navegue pelos períodos do Mesozoico com slider interativo e gráfico de barras.
9. **🌍 Clima Mesozóico**: Explore temperatura, CO₂ e vegetação de cada período geológico.
10. **🏆 Conquistas**: Sistema de progressão com 7 medalhas. Salvo localmente usando DataStore.
11. **📄 Exportar PDF**: Gera um relatório científico completo em PDF usando a API nativa `PdfDocument`.
12. **🌳 Árvore Evolutiva**: Cladograma hierárquico interativo desenhado via Compose Canvas mostrando relações filogenéticas.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: [Kotlin](https://kotlinlang.org/) 2.0.0
- **Interface**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
- **Navegação**: Navigation Compose
- **Persistência**: DataStore Preferences (para conquistas e períodos visitados)
- **Gráficos**: Compose Canvas (nativo, sem dependências externas de terceiros)
- **Mapas e Globo**: WebView com injeção de Leaflet.js
- **Geração de PDF**: `android.graphics.pdf.PdfDocument` (nativo)
- **Arquitetura**: Separação de camadas (UI, Domain, Data)

---

## 🚀 Como executar o projeto

### Pré-requisitos
- Android Studio (versão mais recente recomendada - Koala ou superior)
- JDK 17
- Android SDK 34 (compileSdk 34, minSdk 24)

### Passos
1. Faça o clone deste repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git
   ```
2. Abra o **Android Studio**, clique em **Open** e selecione a pasta do projeto.
3. Aguarde a sincronização do Gradle (na primeira vez ele fará o download das dependências do Compose).
4. Conecte um dispositivo físico (recomendado para melhor desempenho) via USB com a Depuração USB ativada, ou use um Emulador.
5. Clique no botão **Run (Play verde)** para compilar e instalar.

---

## 📁 Estrutura do Projeto

```text
app/src/main/kotlin/com/dino/app/
│
├── MainActivity.kt               # Ponto de entrada do app (Compose)
├── PaleoApplication.kt           # Inicializa as dependências (DataStore)
│
├── data/                         # Camada de Dados
│   ├── AppPreferences.kt         # Configurações salvas (DataStore)
│   ├── DinoData.kt               # Banco de dados local (listas e mapas)
│   └── model/                    # Data classes (Models.kt)
│
├── domain/                       # Camada de Regras de Negócio (Puro Kotlin)
│   ├── Simulacoes.kt             # Fórmulas matemáticas e lógica de extinção
│   └── BatalhaGenerator.kt       # Geração de perguntas do modo Batalha
│
└── ui/                           # Camada de Interface (Compose)
    ├── MainScreen.kt             # Scaffold e BottomNavigation
    ├── components/               # Componentes reutilizáveis (Card, Charts)
    ├── theme/                    # Cores e tipografia
    └── screens/                  # As 12 telas do aplicativo
```

---

## 🎓 Contexto Pedagógico e Fontes

- **Campione & Evans (2012)** – *A universal scaling relationship between body mass and proximal limb bone dimensions in quadrupedal terrestrial tetrapods.*
- **Ancient Earth Globe** – [dinosaurpictures.org/ancient-earth](https://dinosaurpictures.org/ancient-earth)
- **Paleobiology Database** – [paleobiodb.org](https://paleobiodb.org/)
- **Modelo climático** – baseado em Scotese (2021) e IPCC (2021).

---

## 📄 Licença

Este projeto é distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais informações.

---
Desenvolvido com 🦕 e ☕.