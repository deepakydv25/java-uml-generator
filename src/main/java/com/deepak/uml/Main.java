package com.deepak.uml;

import com.deepak.uml.parser.JavaSourceParser;
import com.deepak.uml.analyzer.ClassAnalyzer;
import com.deepak.uml.model.UmlClass;
import com.deepak.uml.generator.DiagramGenerator;
import com.deepak.uml.generator.MermaidGenerator;
import com.deepak.uml.generator.PlantUMLGenerator;
import com.deepak.uml.export.FileExporter;
import com.github.javaparser.ast.CompilationUnit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Main entry point for the Java UML Generator.
 *
 * Usage:
 *   java Main <path-to-java-file> [--format <format>] [--export]
 *
 * Formats:
 *   text      - Human-readable text output (default)
 *   mermaid   - Mermaid class diagram syntax
 *   plantuml  - PlantUML class diagram syntax
 *
 * Flags:
 *   --export  - Save output to file in 'diagrams' directory
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String filePath = args[0];
        String format = "text"; // default format
        boolean shouldExport = false;

        // Parse optional arguments
        for (int i = 1; i < args.length; i++) {
            if ("--format".equals(args[i]) && i + 1 < args.length) {
                format = args[i + 1];
                i++; // Skip next argument as it's the format value
            } else if ("--export".equals(args[i])) {
                shouldExport = true;
            }
        }

        try {
            // Parse the Java source file
            JavaSourceParser parser = new JavaSourceParser();
            CompilationUnit compilationUnit = parser.parseFile(filePath);

            // Analyze and extract UML information
            ClassAnalyzer analyzer = new ClassAnalyzer();
            List<UmlClass> umlClasses = analyzer.analyzeCompilationUnit(compilationUnit);

            // Output based on format
            if (umlClasses.isEmpty()) {
                System.out.println("No classes found in the file.");
            } else {
                outputDiagram(umlClasses, format, filePath, shouldExport);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + filePath);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error: Could not export file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Outputs the UML diagram in the requested format.
     * If shouldExport is true, saves to file; otherwise prints to console.
     */
    private static void outputDiagram(List<UmlClass> umlClasses, String format, String javaFilePath, boolean shouldExport) throws IOException {
        String output;
        String fileExtension;

        switch (format.toLowerCase()) {
            case "mermaid":
                DiagramGenerator mermaidGen = new MermaidGenerator();
                output = mermaidGen.generate(umlClasses);
                fileExtension = ".mmd";
                break;

            case "plantuml":
                DiagramGenerator plantGen = new PlantUMLGenerator();
                output = plantGen.generate(umlClasses);
                fileExtension = ".puml";
                break;

            case "text":
            default:
                // For text format, concatenate all class outputs
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < umlClasses.size(); i++) {
                    sb.append(umlClasses.get(i));
                    if (i < umlClasses.size() - 1) {
                        sb.append("\n").append("=".repeat(60)).append("\n\n");
                    }
                }
                output = sb.toString();
                fileExtension = ".txt";
                break;
        }

        if (shouldExport) {
            // Export to file
            String baseFilename = FileExporter.generateFilename(javaFilePath);
            String filename = baseFilename + fileExtension;
            String filePath = FileExporter.exportToFile(output, filename);
            System.out.println("✓ Diagram exported successfully!");
            System.out.println("✓ File: " + filePath);
            System.out.println();
            System.out.println("Content preview:");
            System.out.println(output);
        } else {
            // Print to console
            System.out.println(output);
        }
    }

    private static void printUsage() {
        System.err.println("Java UML Generator");
        System.err.println();
        System.err.println("Usage: java Main <path-to-java-file> [--format <format>] [--export]");
        System.err.println();
        System.err.println("Formats:");
        System.err.println("  text      - Human-readable text output (default)");
        System.err.println("  mermaid   - Mermaid class diagram syntax");
        System.err.println("  plantuml  - PlantUML class diagram syntax");
        System.err.println();
        System.err.println("Flags:");
        System.err.println("  --export  - Save output to file in 'diagrams' directory");
        System.err.println();
        System.err.println("Examples:");
        System.err.println("  java Main src/samples/Order.java");
        System.err.println("  java Main src/samples/Order.java --format mermaid");
        System.err.println("  java Main src/samples/Order.java --format mermaid --export");
        System.err.println("  java Main src/samples/Order.java --export");
    }
}
