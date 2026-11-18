#!/usr/bin/env bash
# Baixa cópias locais das bibliotecas usadas pelo site (gráficos, mapa,
# árvore evolutiva e PDF) para app/src/main/assets/www/vendor.
# Assim essas telas funcionam sem internet. O GitHub Actions roda este
# script automaticamente antes de gerar o APK.
set -euo pipefail

RAIZ="$(cd "$(dirname "$0")/.." && pwd)"
DESTINO="${1:-$RAIZ/app/src/main/assets/www/vendor}"
mkdir -p "$DESTINO/leaflet/images"

baixar() {  # baixar <url> <arquivo>
    echo "  ↓ ${2#$RAIZ/}"
    curl -fsSL --retry 3 "$1" -o "$2"
}

echo "Baixando bibliotecas..."
baixar https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js               "$DESTINO/chart.umd.min.js"
baixar https://cdn.jsdelivr.net/npm/vis-network@9.1.2/standalone/umd/vis-network.min.js "$DESTINO/vis-network.min.js"
baixar https://cdn.jsdelivr.net/npm/jspdf@2.5.1/dist/jspdf.umd.min.js                   "$DESTINO/jspdf.umd.min.js"
baixar https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.js                       "$DESTINO/leaflet/leaflet.js"
baixar https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.css                      "$DESTINO/leaflet/leaflet.css"
for img in layers.png layers-2x.png marker-icon.png marker-icon-2x.png marker-shadow.png; do
    baixar "https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/$img" "$DESTINO/leaflet/images/$img"
done
echo "Pronto."
