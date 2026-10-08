@echo off
chcp 65001 > nul
echo ========================================================
echo    Organizador de Arquivos Desktop - Compilar e Executar
echo ========================================================
echo.

set "JAVAC_CMD=javac"
set "JAVA_CMD=java"

where javac >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot\bin\javac.exe" (
        set "JAVAC_CMD=C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot\bin\javac.exe"
        set "JAVA_CMD=C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot\bin\java.exe"
    ) else (
        echo [AVISO] Compilador javac não encontrado no PATH.
        pause
        exit /b 1
    )
)

echo [1/2] Compilando arquivos Java...
if not exist "bin" mkdir "bin"

"%JAVAC_CMD%" -encoding UTF-8 -d bin src\com\gabrila7\fileorganizer\model\*.java src\com\gabrila7\fileorganizer\service\*.java src\com\gabrila7\fileorganizer\ui\*.java src\com\gabrila7\fileorganizer\*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Falha na compilação.
    pause
    exit /b 1
)

echo [2/2] Executando aplicação...
echo.
"%JAVA_CMD%" -cp bin com.gabrila7.fileorganizer.Main
