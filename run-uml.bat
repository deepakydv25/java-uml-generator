@echo off
REM Run Java UML Generator
REM Usage: run-uml.bat <path-to-java-file>

if "%1"=="" (
    echo Usage: run-uml.bat ^<path-to-java-file^>
    exit /b 1
)

setlocal enabledelayedexpansion
set "REPO=%USERPROFILE%\.m2\repository"
set "CP=target\classes;!REPO!\com\github\javaparser\javaparser-core\3.25.9\javaparser-core-3.25.9.jar"

java -cp !CP! com.deepak.uml.Main %1
endlocal
