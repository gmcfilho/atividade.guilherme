@echo off

echo.
echo   📚 Sistema de Biblioteca Escolar
echo   =================================

:: Cria pasta de saída
if not exist out mkdir out

:: Compila todos os .java dentro de src/
echo   Compilando...
for /r src %%f in (*.java) do set SOURCES=!SOURCES! "%%f"
javac -d out -sourcepath src src\Main.java src\model\*.java src\service\*.java src\exception\*.java src\ui\*.java

if errorlevel 1 (
    echo.
    echo   ERRO na compilacao. Verifique os arquivos .java.
    pause
    exit /b 1
)

echo   Compilado com sucesso!
echo.

:: Executa
java -cp out Main

pause
