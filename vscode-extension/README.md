Java UML Generator - VS Code Extension

Generate a Mermaid UML class diagram from a Java project with one command or from a Java file's Explorer context menu.

Usage
1. Install the extension package.

2. Open a Java project in VS Code.

3. Right-click a Java file in Explorer or run "Generate UML (Java UML Generator)" from the Command Palette.

Notes
- The extension bundles the CLI JAR and writes the generated diagram to `diagrams/<JavaFile>.mmd`.
- A workspace JAR at `<workspace>/target/java-uml-generator-1.0-SNAPSHOT.jar` is used as a development fallback.
- The project requires a Java runtime available on the user's PATH.

Local development
```powershell
npm install
npm run build
```