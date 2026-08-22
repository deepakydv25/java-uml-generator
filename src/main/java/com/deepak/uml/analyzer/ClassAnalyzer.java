package com.deepak.uml.analyzer;

import com.deepak.uml.model.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.Modifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes Java AST and extracts UML class information.
 */
public class ClassAnalyzer {

    /**
     * Analyzes a CompilationUnit AST and extracts all UML classes.
     *
     * @param compilationUnit the JavaParser CompilationUnit AST
     * @return a list of extracted UmlClass objects
     */
    public List<UmlClass> analyzeCompilationUnit(CompilationUnit compilationUnit) {
        List<UmlClass> umlClasses = new ArrayList<>();
        String packageName = compilationUnit.getPackageDeclaration()
                .map(pd -> pd.getNameAsString())
                .orElse("(default package)");

        // Extract classes and interfaces
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
                .forEach(typeDecl -> {
                    UmlClass umlClass = analyzeClassOrInterface(typeDecl, packageName);
                    umlClasses.add(umlClass);
                });

        // Extract enums
        compilationUnit.findAll(EnumDeclaration.class)
                .forEach(enumDecl -> {
                    UmlClass umlClass = analyzeEnum(enumDecl, packageName);
                    umlClasses.add(umlClass);
                });

        // Extract annotations
        compilationUnit.findAll(AnnotationDeclaration.class)
                .forEach(annotationDecl -> {
                    UmlClass umlClass = analyzeAnnotation(annotationDecl, packageName);
                    umlClasses.add(umlClass);
                });

        return umlClasses;
    }

    /**
     * Analyzes a ClassOrInterfaceDeclaration and extracts UML information.
     */
    private UmlClass analyzeClassOrInterface(ClassOrInterfaceDeclaration typeDecl, String packageName) {
        ClassType classType = typeDecl.isInterface() ? ClassType.INTERFACE : ClassType.CLASS;
        UmlClass umlClass = new UmlClass(typeDecl.getNameAsString(), packageName, classType);

        // Extract fields
        typeDecl.getFields().forEach(field -> extractField(field, umlClass));

        // Extract methods
        typeDecl.getMethods().forEach(method -> extractMethod(method, umlClass));

        return umlClass;
    }

    /**
     * Analyzes an EnumDeclaration and extracts UML information.
     */
    private UmlClass analyzeEnum(EnumDeclaration enumDecl, String packageName) {
        UmlClass umlClass = new UmlClass(enumDecl.getNameAsString(), packageName, ClassType.ENUM);

        // Extract enum fields
        enumDecl.getFields().forEach(field -> extractField(field, umlClass));

        // Extract enum methods
        enumDecl.getMethods().forEach(method -> extractMethod(method, umlClass));

        return umlClass;
    }

    /**
     * Analyzes an AnnotationDeclaration and extracts UML information.
     */
    private UmlClass analyzeAnnotation(AnnotationDeclaration annotationDecl, String packageName) {
        UmlClass umlClass = new UmlClass(annotationDecl.getNameAsString(), packageName, ClassType.ANNOTATION);

        // Extract annotation members (treated as fields)
        annotationDecl.getMembers().forEach(member -> {
            if (member.isAnnotationMemberDeclaration()) {
                String memberName = member.asAnnotationMemberDeclaration().getNameAsString();
                String memberType = member.asAnnotationMemberDeclaration().getType().asString();
                UmlField field = new UmlField(memberName, memberType, Visibility.PUBLIC);
                umlClass.addField(field);
            }
        });

        return umlClass;
    }

    /**
     * Extracts fields from a FieldDeclaration and adds them to the UmlClass.
     * A FieldDeclaration can declare multiple variables (e.g., int x, y, z;).
     */
    private void extractField(FieldDeclaration fieldDecl, UmlClass umlClass) {
        Visibility visibility = getVisibility(fieldDecl);
        String type = fieldDecl.getCommonType().asString();

        // Each variable in the field declaration becomes a separate UmlField
        fieldDecl.getVariables().forEach(variable -> {
            String fieldName = variable.getNameAsString();
            UmlField field = new UmlField(fieldName, type, visibility);
            umlClass.addField(field);
        });
    }

    /**
     * Extracts methods from a MethodDeclaration and adds them to the UmlClass.
     */
    private void extractMethod(MethodDeclaration methodDecl, UmlClass umlClass) {
        Visibility visibility = getVisibility(methodDecl);
        String returnType = methodDecl.getTypeAsString();
        String methodName = methodDecl.getNameAsString();

        UmlMethod method = new UmlMethod(methodName, returnType, visibility);

        // Extract parameters
        methodDecl.getParameters().forEach(param -> {
            String paramName = param.getNameAsString();
            String paramType = param.getTypeAsString();
            UmlParameter parameter = new UmlParameter(paramName, paramType);
            method.addParameter(parameter);
        });

        umlClass.addMethod(method);
    }

    /**
     * Determines visibility from a field or method declaration.
     */
    private Visibility getVisibility(FieldDeclaration decl) {
        if (decl.isPublic()) return Visibility.PUBLIC;
        if (decl.isProtected()) return Visibility.PROTECTED;
        if (decl.isPrivate()) return Visibility.PRIVATE;
        return Visibility.PACKAGE;
    }

    /**
     * Determines visibility from a method declaration.
     */
    private Visibility getVisibility(MethodDeclaration decl) {
        if (decl.isPublic()) return Visibility.PUBLIC;
        if (decl.isProtected()) return Visibility.PROTECTED;
        if (decl.isPrivate()) return Visibility.PRIVATE;
        return Visibility.PACKAGE;
    }
}
