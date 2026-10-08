@echo off
chcp 65001 > nul
set "JAVA_CMD=java"

if exist "C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot\bin\java.exe"
)

"%JAVA_CMD%" -cp bin com.gabrila7.fileorganizer.Main
