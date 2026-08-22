package com.deepak.uml.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Parses a Java source file using JavaParser and returns the CompilationUnit AST.
 */
public class JavaSourceParser {
    private JavaParser javaParser;

    public JavaSourceParser() {
        this.javaParser = new JavaParser();
    }

    /**
     * Parses a Java source file from the given file path.
     *
     * @param filePath the path to the .java source file
     * @return the parsed CompilationUnit AST
     * @throws FileNotFoundException if the file is not found
     */
    public CompilationUnit parseFile(String filePath) throws FileNotFoundException {
        return javaParser.parse(new File(filePath)).getResult()
                .orElseThrow(() -> new RuntimeException("Failed to parse file: " + filePath));
    }

    /**
     * Parses Java source code from a String.
     *
     * @param sourceCode the Java source code as a String
     * @return the parsed CompilationUnit AST
     */
    public CompilationUnit parseString(String sourceCode) {
        return javaParser.parse(sourceCode).getResult()
                .orElseThrow(() -> new RuntimeException("Failed to parse source code"));
    }
}
