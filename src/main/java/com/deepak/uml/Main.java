//package com.deepak.uml;
//
//import com.deepak.uml.parser.JavaSourceParser;
//import com.deepak.uml.analyzer.ClassAnalyzer;
//import com.deepak.uml.model.UmlClass;
//import com.deepak.uml.generator.DiagramGenerator;
//import com.deepak.uml.generator.MermaidGenerator;
//import com.deepak.uml.generator.PlantUMLGenerator;
//import com.deepak.uml.export.FileExporter;
//import com.github.javaparser.ast.CompilationUnit;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.List;
//
///**
// * Main entry point for the Java UML Generator.
// *
// * Usage:
// *   java Main <path-to-java-file> [--format <format>] [--export]
// *
// * Formats:
// *   text      - Human-readable text output (default)
// *   mermaid   - Mermaid class diagram syntax
// *   plantuml  - PlantUML class diagram syntax
// *
// * Flags:
// *   --export  - Save output to file in 'diagrams' directory
// */
//public class Main {
//
//    public static void main(String[] args) {
//        if (args.length == 0) {
//            printUsage();
//            System.exit(1);
//        }
//
//        String filePath = args[0];
//        String format = "text"; // default format
//        boolean shouldExport = false;
//
//        // Parse optional arguments
//        for (int i = 1; i < args.length; i++) {
//            if ("--format".equals(args[i]) && i + 1 < args.length) {
//                format = args[i + 1];
//                i++; // Skip next argument as it's the format value
//            } else if ("--export".equals(args[i])) {
//                shouldExport = true;
//            }
//        }
//
//        try {
//            // Parse the Java source file
//            JavaSourceParser parser = new JavaSourceParser();
//            CompilationUnit compilationUnit = parser.parseFile(filePath);
//
//            // Analyze and extract UML information
//            ClassAnalyzer analyzer = new ClassAnalyzer();
//            List<UmlClass> umlClasses = analyzer.analyzeCompilationUnit(compilationUnit);
//
//            // Output based on format
//            if (umlClasses.isEmpty()) {
//                System.out.println("No classes found in the file.");
//            } else {
//                outputDiagram(umlClasses, format, filePath, shouldExport);
//            }
//        } catch (FileNotFoundException e) {
//            System.err.println("Error: File not found: " + filePath);
//            e.printStackTrace();
//            System.exit(1);
//        } catch (IOException e) {
//            System.err.println("Error: Could not export file: " + e.getMessage());
//            e.printStackTrace();
//            System.exit(1);
//        } catch (Exception e) {
//            System.err.println("Error processing file: " + e.getMessage());
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
//
//    /**
//     * Outputs the UML diagram in the requested format.
//     * If shouldExport is true, saves to file; otherwise prints to console.
//     */
//    private static void outputDiagram(List<UmlClass> umlClasses, String format, String javaFilePath, boolean shouldExport) throws IOException {
//        String output;
//        String fileExtension;
//
//        switch (format.toLowerCase()) {
//            case "mermaid":
//                DiagramGenerator mermaidGen = new MermaidGenerator();
//                output = mermaidGen.generate(umlClasses);
//                fileExtension = ".mmd";
//                break;
//
//            case "plantuml":
//                DiagramGenerator plantGen = new PlantUMLGenerator();
//                output = plantGen.generate(umlClasses);
//                fileExtension = ".puml";
//                break;
//
//            case "text":
//            default:
//                // For text format, concatenate all class outputs
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < umlClasses.size(); i++) {
//                    sb.append(umlClasses.get(i));
//                    if (i < umlClasses.size() - 1) {
//                        sb.append("\n").append("=".repeat(60)).append("\n\n");
//                    }
//                }
//                output = sb.toString();
//                fileExtension = ".txt";
//                break;
//        }
//
//        if (shouldExport) {
//            // Export to file
//            String baseFilename = FileExporter.generateFilename(javaFilePath);
//            String filename = baseFilename + fileExtension;
//            String filePath = FileExporter.exportToFile(output, filename);
//            System.out.println("✓ Diagram exported successfully!");
//            System.out.println("✓ File: " + filePath);
//            System.out.println();
//            System.out.println("Content preview:");
//            System.out.println(output);
//        } else {
//            // Print to console
//            System.out.println(output);
//        }
//    }
//
//    private static void printUsage() {
//        System.err.println("Java UML Generator");
//        System.err.println();
//        System.err.println("Usage: java Main <path-to-java-file> [--format <format>] [--export]");
//        System.err.println();
//        System.err.println("Formats:");
//        System.err.println("  text      - Human-readable text output (default)");
//        System.err.println("  mermaid   - Mermaid class diagram syntax");
//        System.err.println("  plantuml  - PlantUML class diagram syntax");
//        System.err.println();
//        System.err.println("Flags:");
//        System.err.println("  --export  - Save output to file in 'diagrams' directory");
//        System.err.println();
//        System.err.println("Examples:");
//        System.err.println("  java Main src/samples/Order.java");
//        System.err.println("  java Main src/samples/Order.java --format mermaid");
//        System.err.println("  java Main src/samples/Order.java --format mermaid --export");
//        System.err.println("  java Main src/samples/Order.java --export");
//    }
//}


package com.deepak.uml;

import com.deepak.uml.analyzer.ClassAnalyzer;
import com.deepak.uml.generator.DiagramGenerator;
import com.deepak.uml.generator.MermaidGenerator;
import com.deepak.uml.generator.PlantUMLGenerator;
import com.deepak.uml.model.UmlClass;
import com.deepak.uml.parser.JavaProjectParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            if (args.length == 0) {
                printUsage();
                return;
            }

            String command = args[0];

            if ("generate".equalsIgnoreCase(command)) {
                generateDiagram(args);
            } else {
                System.out.println("Unknown command: " + command);
                printUsage();
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void generateDiagram(String[] args) throws IOException {

        // Default values
        Path inputPath = Paths.get(".");
        String format = "mermaid";
        Path outputPath = Paths.get("uml", "class-diagram.mmd");

        // Parse command-line arguments
        int i = 1;

        // Optional input directory
        if (i < args.length && !args[i].startsWith("--")) {
            inputPath = Paths.get(args[i]);
            i++;
        }

        while (i < args.length) {

            String argument = args[i];

            switch (argument) {

                case "--format":
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException(
                                "Missing value for --format"
                        );
                    }

                    format = args[++i].toLowerCase();
                    break;

                case "--output":
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException(
                                "Missing value for --output"
                        );
                    }

                    outputPath = Paths.get(args[++i]);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unknown option: " + argument
                    );
            }

            i++;
        }

        // Validate input
        if (!Files.exists(inputPath)) {
            throw new IllegalArgumentException(
                    "Input path does not exist: " + inputPath
            );
        }

        if (!Files.isDirectory(inputPath)) {
            throw new IllegalArgumentException(
                    "Input path must be a directory: " + inputPath
            );
        }

        System.out.println("Scanning Java project: " +
                inputPath.toAbsolutePath());

        // Step 1: Parse all Java files
        JavaProjectParser projectParser = new JavaProjectParser();

        List<com.github.javaparser.ast.CompilationUnit> compilationUnits =
                projectParser.parseDirectory(inputPath);

        if (compilationUnits.isEmpty()) {
            System.out.println("No Java files found.");
            return;
        }

        System.out.println(
                "Java files found: " + compilationUnits.size()
        );

        // Step 2: Analyze all Java files
        ClassAnalyzer analyzer = new ClassAnalyzer();

        List<UmlClass> umlClasses =
                analyzer.analyzeCompilationUnits(compilationUnits);

        System.out.println(
                "Classes found: " + umlClasses.size()
        );

        // Step 3: Select diagram generator
        DiagramGenerator generator;

        switch (format) {

            case "mermaid":
                generator = new MermaidGenerator();
                break;

            case "plantuml":
                generator = new PlantUMLGenerator();
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported format: " + format +
                                ". Supported formats: mermaid, plantuml"
                );
        }

        // Step 4: Generate diagram
        String diagram = generator.generate(umlClasses);

        // Step 5: Make sure output directory exists
        Path parentDirectory = outputPath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        // Step 6: Write diagram to file
        Files.writeString(outputPath, diagram);

        System.out.println();
        System.out.println("UML class diagram generated successfully.");
        System.out.println(
                "Output: " + outputPath.toAbsolutePath()
        );
    }

    private static void printUsage() {

        System.out.println();
        System.out.println("Java UML Generator");
        System.out.println();
        System.out.println("Usage:");
        System.out.println(
                "  generate [input-directory] [options]"
        );
        System.out.println();
        System.out.println("Examples:");
        System.out.println(
                "  generate"
        );
        System.out.println(
                "  generate ./src"
        );
        System.out.println(
                "  generate ./src --format mermaid"
        );
        System.out.println(
                "  generate ./src --format plantuml"
        );
        System.out.println(
                "  generate ./src --output uml/class-diagram.mmd"
        );
        System.out.println();
        System.out.println("Options:");
        System.out.println(
                "  --format mermaid|plantuml"
        );
        System.out.println(
                "  --output <file>"
        );
        System.out.println();
    }
}
