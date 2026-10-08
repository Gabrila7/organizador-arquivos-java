@echo off
chcp 65001 > nul
echo ========================================================
echo    Organizador de Arquivos Desktop - Compilar e Executar
echo ========================================================
echo.

where javac >nul 2>nul
if %errorlevel% neq 0 (
    echo [AVISO] O compilador 'javac' não foi encontrado no PATH do sistema.
    echo.
    echo Se você usa uma IDE (IntelliJ IDEA, Eclipse, VS Code), abra esta
    echo pasta diretamente pela IDE e execute a classe 'Main.java'.
    echo.
    echo Para instalar o JDK no Windows via terminal, use:
    echo   winget install EclipseAdoptium.Temurin.17.JDK
    echo.
    pause
    exit /b 1
)

echo [1/2] Compilando arquivos Java...
if not exist "bin" mkdir "bin"

javac -encoding UTF-8 -d bin src\com\gabrila7\fileorganizer\model\*.java src\com\gabrila7\fileorganizer\service\*.java src\com\gabrila7\fileorganizer\ui\*.java src\com\gabrila7\fileorganizer\*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Falha na compilação. Verifique os erros acima.
    pause
    exit /b 1
)

echo [2/2] Executando a aplicação...
echo.
java -cp bin com.gabrila7.fileorganizer.Main

pause
