package com.deepak.uml.parser;

import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class JavaProjectParser {

    private final JavaSourceParser sourceParser;

    public JavaProjectParser() {
        this.sourceParser = new JavaSourceParser();
    }

    public List<CompilationUnit> parseDirectory(Path directory)
            throws IOException {

        try (Stream<Path> paths = Files.walk(directory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(this::parseFile)
                    .toList();
        }
    }

    private CompilationUnit parseFile(Path path) {
        try {
            return sourceParser.parseFile(path.toString());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to parse Java file: " + path,
                    e
            );
        }
    }
}