# Run Java UML Generator with diagram output
# Usage: .\run-uml-diagram.ps1 <path-to-java-file> [format]
# Formats: mermaid (default), plantuml, text

param(
    [Parameter(Mandatory=$true)]
    [string]$FilePath,
    
    [string]$Format = "mermaid"
)

if (-not (Test-Path $FilePath)) {
    Write-Error "File not found: $FilePath"
    exit 1
}

$REPO = "$env:USERPROFILE\.m2\repository"
$JAVAPARSER_JAR = "$REPO\com\github\javaparser\javaparser-core\3.25.9\javaparser-core-3.25.9.jar"

if (-not (Test-Path $JAVAPARSER_JAR)) {
    Write-Error "JavaParser JAR not found at: $JAVAPARSER_JAR"
    Write-Host "Did you run 'mvn clean compile'?"
    exit 1
}

$CP = "target\classes;$JAVAPARSER_JAR"

# Run the Java program with format argument
java -cp $CP com.deepak.uml.Main $FilePath --format $Format
