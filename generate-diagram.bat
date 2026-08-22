@echo off
REM One-Click Diagram Generator for Windows
REM Usage: generate-diagram.bat <path-to-java-file> [format]
REM Formats: mermaid (default), plantuml, text
REM Output: Automatically saved to 'diagrams' folder

setlocal enabledelayedexpansion

if "%1"=="" (
    echo.
    echo ❌ ERROR: Java file path is required
    echo.
    echo Usage: generate-diagram.bat ^<path-to-java-file^> [format]
    echo.
    echo Formats: mermaid (default), plantuml, text
    echo.
    echo Examples:
    echo   generate-diagram.bat src\samples\Order.java
    echo   generate-diagram.bat src\samples\Order.java mermaid
    echo   generate-diagram.bat src\samples\Order.java plantuml
    echo.
    exit /b 1
)

set "JAVA_FILE=%1"
set "FORMAT=%2"

if "!FORMAT!"=="" (
    set "FORMAT=mermaid"
)

REM Check if file exists
if not exist "!JAVA_FILE!" (
    echo.
    echo ❌ ERROR: File not found: !JAVA_FILE!
    echo.
    exit /b 1
)

REM Setup classpath
set "REPO=%USERPROFILE%\.m2\repository"
set "JAVAPARSER_JAR=!REPO!\com\github\javaparser\javaparser-core\3.25.9\javaparser-core-3.25.9.jar"

if not exist "!JAVAPARSER_JAR!" (
    echo.
    echo ❌ ERROR: JavaParser JAR not found
    echo Did you run 'mvn clean compile'?
    echo.
    exit /b 1
)

set "CP=target\classes;!JAVAPARSER_JAR!"

REM Extract class name from file path
for %%F in ("!JAVA_FILE!") do set "FILENAME=%%~nF"
for /F "tokens=* delims=." %%A in ("!FILENAME!") do set "CLASSNAME=%%A"

echo.
echo 📊 Generating !FORMAT! diagram for: !CLASSNAME!

REM Run with --export flag
java -cp "!CP!" com.deepak.uml.Main "!JAVA_FILE!" --format !FORMAT! --export

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ SUCCESS!
    echo 📁 Diagram saved to: diagrams\!CLASSNAME!.*
    echo.
    if "!FORMAT!"=="mermaid" (
        echo 💡 Tip: Use this in GitHub README.md:
        echo     ```mermaid
        echo     [paste content from diagrams\!CLASSNAME!.mmd]
        echo     ```
    )
) else (
    echo.
    echo ❌ Failed to generate diagram
    exit /b 1
)

endlocal
