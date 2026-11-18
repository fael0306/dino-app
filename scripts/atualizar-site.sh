#!/usr/bin/env bash
# Copia o site PaleoLab (repositório fael0306/dino) para dentro do app,
# em app/src/main/assets/www. Rode sempre que o site ganhar uma versão nova.
#
# Uso:
#   ./scripts/atualizar-site.sh                    # baixa a versão atual do GitHub (branch main)
#   ./scripts/atualizar-site.sh ../dino            # usa uma cópia local do repositório do site
#   SITE_BRANCH=dev ./scripts/atualizar-site.sh    # outra branch
set -euo pipefail

RAIZ="$(cd "$(dirname "$0")/.." && pwd)"
DESTINO="$RAIZ/app/src/main/assets/www"
REPO_SITE="${SITE_REPO:-https://github.com/fael0306/dino.git}"
BRANCH="${SITE_BRANCH:-main}"

if [[ $# -ge 1 ]]; then
    ORIGEM="$(cd "$1" && pwd)"
else
    TEMP="$(mktemp -d)"
    trap 'rm -rf "$TEMP"' EXIT
    echo "Baixando o site ($REPO_SITE, branch $BRANCH)..."
    git clone --quiet --depth 1 --branch "$BRANCH" "$REPO_SITE" "$TEMP/dino"
    ORIGEM="$TEMP/dino"
fi

for item in index.html css js assets vendor; do
    if [[ ! -e "$ORIGEM/$item" ]]; then
        echo "Erro: '$item' não encontrado em $ORIGEM. Este é mesmo o repositório do site?" >&2
        exit 1
    fi
done
if [[ ! -f "$ORIGEM/js/plataforma.js" ]]; then
    echo "Erro: o site em $ORIGEM não tem o modo aplicativo (js/plataforma.js). Use a v2.0.0 ou mais nova." >&2
    exit 1
fi

echo "Copiando para $DESTINO ..."
rm -rf "$DESTINO"
mkdir -p "$DESTINO"
cp -R "$ORIGEM/index.html" "$ORIGEM/css" "$ORIGEM/js" "$ORIGEM/assets" "$ORIGEM/vendor" "$DESTINO/"

# Arquivos que não são usados pelo app
find "$DESTINO" -name ".DS_Store" -delete
rm -f "$DESTINO/assets/real_euhelopus_zdanskyi.jpg"   # duplicata da versão .png

if [[ -d "$ORIGEM/.git" ]]; then
    git -C "$ORIGEM" rev-parse --short HEAD > "$DESTINO/VERSAO_SITE.txt" 2>/dev/null || true
fi
echo "Pronto. Site copiado ($(du -sh "$DESTINO" | cut -f1))."
echo "Dica: rode ./scripts/baixar-bibliotecas.sh para o app funcionar 100% offline."
