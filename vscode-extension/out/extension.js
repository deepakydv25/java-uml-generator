"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.deactivate = exports.activate = void 0;
const vscode = __importStar(require("vscode"));
const child_process_1 = require("child_process");
const path = __importStar(require("path"));
const fs = __importStar(require("fs"));
function activate(context) {
    const disposable = vscode.commands.registerCommand('javaUmlGenerator.generateUml', async (uri) => {
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
        const process = (0, child_process_1.spawn)('java', [
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
exports.activate = activate;
function deactivate() { }
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map