#!/bin/bash

echo "📝 Criando o arquivo .gitignore para proteger seus dados locais..."

# Cria o .gitignore com o conteúdo exato
cat > .gitignore << 'EOF'
*.iml
.gradle
/local.properties
/.idea
.DS_Store
/build
/app/build
EOF

echo "✅ Arquivo .gitignore criado com sucesso na raiz do projeto!"
echo "--------------------------------------------------------"
echo "🚀 Tudo pronto! Para subir para o GitHub, basta rodar os comandos do Git:"
echo ""
echo "1. git init"
echo "2. git add ."
echo "3. git commit -m 'Versão inicial do PaleoLab'"
echo "4. git branch -M main"
echo "5. git remote add origin https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git"
echo "6. git push -u origin main"
echo "--------------------------------------------------------"
