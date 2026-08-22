# One-Click Diagram Generator
# Usage: .\generate-diagram.ps1 <path-to-java-file> [format]
# Formats: mermaid (default), plantuml, text
# Output: Automatically saved to 'diagrams' folder

param(
    [Parameter(Mandatory=$true)]
    [string]$FilePath,
    
    [string]$Format = "mermaid"
)

# Validate file exists
if (-not (Test-Path $FilePath)) {
    Write-Error "❌ File not found: $FilePath"
    exit 1
}

# Setup classpath
$REPO = "$env:USERPROFILE\.m2\repository"
$JAVAPARSER_JAR = "$REPO\com\github\javaparser\javaparser-core\3.25.9\javaparser-core-3.25.9.jar"

if (-not (Test-Path $JAVAPARSER_JAR)) {
    Write-Error "❌ JavaParser JAR not found"
    Write-Host "Did you run 'mvn clean compile'?"
    exit 1
}

$CP = "target\classes;$JAVAPARSER_JAR"

# Get the class name from file
$ClassName = [System.IO.Path]::GetFileNameWithoutExtension($FilePath)

# Get the file extension
$Extension = switch ($Format.ToLower()) {
    "mermaid" { ".mmd" }
    "plantuml" { ".puml" }
    default { ".txt" }
}

# Run with --export flag
Write-Host "📊 Generating $Format diagram for: $ClassName"
java -cp $CP com.deepak.uml.Main $FilePath --format $Format --export

if ($LASTEXITCODE -eq 0) {
    $OutputFile = "diagrams\$ClassName$Extension"
    Write-Host ""
    Write-Host "✅ SUCCESS!"
    Write-Host "📁 Diagram saved to: $OutputFile"
    Write-Host ""
    if ($Format -eq "mermaid") {
        Write-Host "💡 Tip: Use this in GitHub README.md:"
        Write-Host '    ```mermaid'
        Write-Host "    [paste content from $OutputFile]"
        Write-Host '    ```'
    }
} else {
    Write-Error "❌ Failed to generate diagram"
    exit 1
}
