package com.deepak.uml.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles exporting diagrams to files.
 * Automatically creates output directories and saves diagrams.
 */
public class FileExporter {

    private static final String DEFAULT_OUTPUT_DIR = "diagrams";

    /**
     * Exports content to a file with automatic directory creation.
     *
     * @param content the diagram/text content to export
     * @param filename the filename (e.g., "MyClass.mmd")
     * @return the full path of the created file
     * @throws IOException if file creation fails
     */
    public static String exportToFile(String content, String filename) throws IOException {
        return exportToFile(content, filename, DEFAULT_OUTPUT_DIR);
    }

    /**
     * Exports content to a file in a specific directory.
     *
     * @param content the diagram/text content to export
     * @param filename the filename (e.g., "MyClass.mmd")
     * @param outputDir the output directory (will be created if doesn't exist)
     * @return the full path of the created file
     * @throws IOException if file creation fails
     */
    public static String exportToFile(String content, String filename, String outputDir) throws IOException {
        // Create output directory if it doesn't exist
        Path outputPath = Paths.get(outputDir);
        Files.createDirectories(outputPath);

        // Create full file path
        Path filePath = outputPath.resolve(filename);

        // Write content to file
        Files.write(filePath, content.getBytes());

        return filePath.toAbsolutePath().toString();
    }

    /**
     * Gets the appropriate file extension based on format.
     *
     * @param format the output format (mermaid, plantuml, text)
     * @return the file extension (.mmd, .puml, .txt)
     */
    public static String getFileExtension(String format) {
        return switch (format.toLowerCase()) {
            case "mermaid" -> ".mmd";
            case "plantuml" -> ".puml";
            case "text" -> ".txt";
            default -> ".txt";
        };
    }

    /**
     * Generates a filename from a Java class name.
     * Example: "MyClass.java" → "MyClass"
     *
     * @param javaFilePath the path to the Java file
     * @return the class name without extension
     */
    public static String generateFilename(String javaFilePath) {
        File file = new File(javaFilePath);
        String filename = file.getName();
        
        // Remove .java extension if present
        if (filename.endsWith(".java")) {
            return filename.substring(0, filename.length() - 5);
        }
        return filename;
    }
}
