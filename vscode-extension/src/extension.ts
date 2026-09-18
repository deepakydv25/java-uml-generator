import * as vscode from 'vscode';
import { spawn } from 'child_process';
import * as path from 'path';
import * as fs from 'fs';

export function activate(context: vscode.ExtensionContext) {

    const disposable = vscode.commands.registerCommand('javaUmlGenerator.generateUml', async (uri: vscode.Uri | undefined) => {

        const editor = vscode.window.activeTextEditor;
        const fileUri = uri || (editor && editor.document.uri);

        if (!fileUri) {
            vscode.window.showErrorMessage('Open a Java file or right-click a file in Explorer and run "Generate UML"');
            return;
        }

        const workspaceFolder = vscode.workspace.workspaceFolders ? vscode.workspace.workspaceFolders[0].uri.fsPath : undefined;
        if (!workspaceFolder) {
            vscode.window.showErrorMessage('Open a workspace folder first.');
            return;
        }

        const bundledJarPath = path.join(context.extensionPath, 'cli', 'java-uml-generator.jar');
        const workspaceJarPath = path.join(workspaceFolder, 'target', 'java-uml-generator-1.0-SNAPSHOT.jar');
        const jarPath = fs.existsSync(bundledJarPath) ? bundledJarPath : workspaceJarPath;

        if (!fs.existsSync(jarPath)) {
            vscode.window.showErrorMessage('Java UML Generator CLI is not available. Reinstall the extension or build the project with Maven.');
            return;
        }

        const inputDir = path.dirname(fileUri.fsPath);
        const outDir = path.join(workspaceFolder, 'diagrams');
        if (!fs.existsSync(outDir)) {
            fs.mkdirSync(outDir, { recursive: true });
        }

        const outFile = path.join(outDir, path.basename(fileUri.fsPath, '.java') + '.mmd');

        const process = spawn('java', [
            '-jar',
            jarPath,
            'generate',
            inputDir,
            '--format',
            'mermaid',
            '--output',
            outFile
        ], { cwd: workspaceFolder });

        let errorOutput = '';
        process.stderr.on('data', data => {
            errorOutput += data.toString();
        });

        process.on('error', error => {
            vscode.window.showErrorMessage(`Could not start Java UML Generator: ${error.message}`);
        });

        process.on('close', code => {
            if (code !== 0) {
                vscode.window.showErrorMessage(`UML generation failed${errorOutput ? `: ${errorOutput.trim()}` : '.'}`);
                return;
            }

            const openUri = vscode.Uri.file(outFile);
            vscode.workspace.openTextDocument(openUri)
                .then(document => vscode.window.showTextDocument(document));
            vscode.window.showInformationMessage('UML diagram generated in diagrams/.');
        });
    });

    context.subscriptions.push(disposable);
}

export function deactivate() { }
