@echo off
chcp 65001 > nul
cd /d "%~dp0"

if not exist "bin" mkdir "bin"

echo Compilando projeto...
javac -encoding UTF-8 -d bin src\com\gabrila7\fileorganizer\model\*.java src\com\gabrila7\fileorganizer\service\*.java src\com\gabrila7\fileorganizer\ui\*.java src\com\gabrila7\fileorganizer\*.java

if %errorlevel% neq 0 (
    echo Erro na compilacao. Verifique se o JDK esta instalado e no PATH.
    pause
    exit /b 1
)

java -cp bin com.gabrila7.fileorganizer.Main
