package com.deepak.uml;

import com.deepak.uml.parser.JavaSourceParser;
import com.deepak.uml.analyzer.ClassAnalyzer;
import com.deepak.uml.model.UmlClass;
import com.github.javaparser.ast.CompilationUnit;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Main entry point for the Java UML Generator.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java Main <path-to-java-file>");
            System.exit(1);
        }

        String filePath = args[0];

        try {
            // Parse the Java source file
            JavaSourceParser parser = new JavaSourceParser();
            CompilationUnit compilationUnit = parser.parseFile(filePath);

            // Analyze and extract UML information
            ClassAnalyzer analyzer = new ClassAnalyzer();
            List<UmlClass> umlClasses = analyzer.analyzeCompilationUnit(compilationUnit);

            // Print the extracted UML classes
            if (umlClasses.isEmpty()) {
                System.out.println("No classes found in the file.");
            } else {
                for (int i = 0; i < umlClasses.size(); i++) {
                    System.out.println(umlClasses.get(i));
                    if (i < umlClasses.size() - 1) {
                        System.out.println("\n" + "=".repeat(60) + "\n");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + filePath);
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
