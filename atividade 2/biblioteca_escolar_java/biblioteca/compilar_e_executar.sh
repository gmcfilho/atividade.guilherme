#!/bin/bash

echo ""
echo "  📚 Sistema de Biblioteca Escolar"
echo "  ================================="

# Cria pasta de saída
mkdir -p out

# Compila todos os .java dentro de src/
echo "  🔧 Compilando..."
find src -name "*.java" | xargs javac -d out -sourcepath src

if [ $? -ne 0 ]; then
  echo ""
  echo "  ❌ Erro na compilação. Verifique os arquivos .java."
  exit 1
fi

echo "  ✅ Compilado com sucesso!"
echo ""

# Executa
java -cp out Main
