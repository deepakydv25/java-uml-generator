package com.deepak.uml.analyzer;

import com.deepak.uml.model.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Analyzes Java AST and extracts UML class information including relationships.
 */
public class ClassAnalyzer {

    /**
     * Analyzes a single CompilationUnit.
     *
     * Note:
     * Relationship detection between project classes is performed in
     * analyzeCompilationUnits(), because we need to know all classes
     * in the project before resolving references.
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
     * Analyzes multiple CompilationUnits and extracts all UML classes.
     *
     * Relationship detection is performed after all classes are known.
     */
    public List<UmlClass> analyzeCompilationUnits(
            List<CompilationUnit> compilationUnits) {

        List<UmlClass> umlClasses = new ArrayList<>();

        // Phase 1: Extract all classes/interfaces/enums/annotations.
        for (CompilationUnit compilationUnit : compilationUnits) {
            umlClasses.addAll(analyzeCompilationUnit(compilationUnit));
        }

        // Phase 2: Build set of classes that actually belong to this project.
        Set<String> projectTypes = new HashSet<>();

        for (UmlClass umlClass : umlClasses) {
            projectTypes.add(umlClass.getName());
        }

        // Phase 3: Detect relationships that require knowledge
        // of all project classes.
        for (UmlClass umlClass : umlClasses) {
            detectFieldRelationships(umlClass, projectTypes);
            detectMethodRelationships(umlClass, projectTypes);
        }

        return umlClasses;
    }

    /**
     * Analyzes a class or interface.
     */
    private UmlClass analyzeClassOrInterface(
            ClassOrInterfaceDeclaration typeDecl,
            String packageName) {

        ClassType classType = typeDecl.isInterface()
                ? ClassType.INTERFACE
                : ClassType.CLASS;

        UmlClass umlClass = new UmlClass(
                typeDecl.getNameAsString(),
                packageName,
                classType
        );

        // Inheritance
        typeDecl.getExtendedTypes().forEach(extendedType -> {

            String parentClassName = extendedType.getNameAsString();

            UmlRelationship rel = new UmlRelationship(
                    typeDecl.getNameAsString(),
                    parentClassName,
                    RelationshipType.INHERITANCE
            );

            umlClass.addRelationship(rel);
        });

        // Implementation
        typeDecl.getImplementedTypes().forEach(implementedType -> {

            String interfaceName = implementedType.getNameAsString();

            UmlRelationship rel = new UmlRelationship(
                    typeDecl.getNameAsString(),
                    interfaceName,
                    RelationshipType.IMPLEMENTATION
            );

            umlClass.addRelationship(rel);
        });

        // Fields
        typeDecl.getFields()
                .forEach(field -> extractField(field, umlClass));

        // Methods
        typeDecl.getMethods()
                .forEach(method -> extractMethod(method, umlClass));

        return umlClass;
    }

    /**
     * Analyzes an enum.
     */
    private UmlClass analyzeEnum(
            EnumDeclaration enumDecl,
            String packageName) {

        UmlClass umlClass = new UmlClass(
                enumDecl.getNameAsString(),
                packageName,
                ClassType.ENUM
        );

        // Enum implementations
        enumDecl.getImplementedTypes().forEach(implementedType -> {

            String interfaceName = implementedType.getNameAsString();

            UmlRelationship rel = new UmlRelationship(
                    enumDecl.getNameAsString(),
                    interfaceName,
                    RelationshipType.IMPLEMENTATION
            );

            umlClass.addRelationship(rel);
        });

        // Fields
        enumDecl.getFields()
                .forEach(field -> extractField(field, umlClass));

        // Methods
        enumDecl.getMethods()
                .forEach(method -> extractMethod(method, umlClass));

        return umlClass;
    }

    /**
     * Analyzes an annotation.
     */
    private UmlClass analyzeAnnotation(
            AnnotationDeclaration annotationDecl,
            String packageName) {

        UmlClass umlClass = new UmlClass(
                annotationDecl.getNameAsString(),
                packageName,
                ClassType.ANNOTATION
        );

        annotationDecl.getMembers().forEach(member -> {

            if (member.isAnnotationMemberDeclaration()) {

                String memberName =
                        member.asAnnotationMemberDeclaration()
                                .getNameAsString();

                String memberType =
                        member.asAnnotationMemberDeclaration()
                                .getType()
                                .asString();

                UmlField field = new UmlField(
                        memberName,
                        memberType,
                        Visibility.PUBLIC
                );

                umlClass.addField(field);
            }
        });

        return umlClass;
    }

    /**
     * Extract fields.
     */
    private void extractField(
            FieldDeclaration fieldDecl,
            UmlClass umlClass) {

        Visibility visibility = getVisibility(fieldDecl);

        String type = fieldDecl
                .getCommonType()
                .asString();

        fieldDecl.getVariables().forEach(variable -> {

            String fieldName = variable.getNameAsString();

            UmlField field = new UmlField(
                    fieldName,
                    type,
                    visibility
            );

            umlClass.addField(field);
        });
    }

    /**
     * Extract methods.
     */
    private void extractMethod(
            MethodDeclaration methodDecl,
            UmlClass umlClass) {

        Visibility visibility = getVisibility(methodDecl);

        String returnType = methodDecl.getTypeAsString();

        String methodName = methodDecl.getNameAsString();

        UmlMethod method = new UmlMethod(
                methodName,
                returnType,
                visibility
        );

        methodDecl.getParameters().forEach(param -> {

            String paramName = param.getNameAsString();

            String paramType = param.getTypeAsString();

            UmlParameter parameter = new UmlParameter(
                    paramName,
                    paramType
            );

            method.addParameter(parameter);
        });

        umlClass.addMethod(method);
    }

    /**
     * Detect relationships caused by fields.
     *
     * Example:
     *
     * private PaymentGateway gateway;
     *
     * produces:
     *
     * Order --> PaymentGateway
     */
    private void detectFieldRelationships(
            UmlClass umlClass,
            Set<String> projectTypes) {

        for (UmlField field : umlClass.getFields()) {

            Set<String> referencedTypes =
                    extractReferencedTypes(field.getType());

            for (String referencedType : referencedTypes) {

                if (!projectTypes.contains(referencedType)) {
                    continue;
                }

                if (umlClass.getName().equals(referencedType)) {
                    continue;
                }

                addRelationshipIfNotExists(
                        umlClass,
                        referencedType,
                        RelationshipType.ASSOCIATION
                );
            }
        }
    }

    /**
     * Detect relationships caused by methods.
     *
     * Parameters and return types create DEPENDENCY relationships.
     */
    private void detectMethodRelationships(
            UmlClass umlClass,
            Set<String> projectTypes) {

        for (UmlMethod method : umlClass.getMethods()) {

            // Return type
            Set<String> returnTypes =
                    extractReferencedTypes(method.getReturnType());

            for (String referencedType : returnTypes) {

                addDependencyIfProjectType(
                        umlClass,
                        referencedType,
                        projectTypes
                );
            }

            // Parameters
            for (UmlParameter parameter : method.getParameters()) {

                Set<String> parameterTypes =
                        extractReferencedTypes(parameter.getType());

                for (String referencedType : parameterTypes) {

                    addDependencyIfProjectType(
                            umlClass,
                            referencedType,
                            projectTypes
                    );
                }
            }
        }
    }

    /**
     * Add dependency only if target is a project type.
     */
    private void addDependencyIfProjectType(
            UmlClass umlClass,
            String targetType,
            Set<String> projectTypes) {

        if (!projectTypes.contains(targetType)) {
            return;
        }

        if (umlClass.getName().equals(targetType)) {
            return;
        }

        addRelationshipIfNotExists(
                umlClass,
                targetType,
                RelationshipType.DEPENDENCY
        );
    }

    /**
     * Extract actual project type names from a Java type.
     *
     * Examples:
     *
     * PaymentGateway
     *     -> PaymentGateway
     *
     * List<PaymentGateway>
     *     -> PaymentGateway
     *
     * Map<String, PaymentGateway>
     *     -> PaymentGateway
     *
     * Map<PaymentGateway, Order>
     *     -> PaymentGateway, Order
     */
    private Set<String> extractReferencedTypes(String type) {

        Set<String> types = new HashSet<>();

        if (type == null || type.isBlank()) {
            return types;
        }

        // Remove array notation
        String cleaned = type
                .replace("[]", "")
                .replace("...", "");

        // Remove generic punctuation
        cleaned = cleaned
                .replace("<", " ")
                .replace(">", " ")
                .replace(",", " ");

        // Split by whitespace
        String[] tokens = cleaned.trim().split("\\s+");

        for (String token : tokens) {

            if (token.isBlank()) {
                continue;
            }

            // Remove wildcard
            token = token.replace("?", "");

            // Remove extends/super from generic declarations
            if (token.equals("extends") || token.equals("super")) {
                continue;
            }

            // If fully qualified, keep only class name.
            int lastDot = token.lastIndexOf('.');

            if (lastDot >= 0) {
                token = token.substring(lastDot + 1);
            }

            if (isIgnoredType(token)) {
                continue;
            }

            types.add(token);
        }

        return types;
    }

    /**
     * Types that should never become UML project relationships.
     */
    private boolean isIgnoredType(String type) {

        return switch (type) {

            // Primitive types
            case "byte",
                 "short",
                 "int",
                 "long",
                 "float",
                 "double",
                 "boolean",
                 "char",
                 "void" -> true;

            // Common Java types
            case "String",
                 "Integer",
                 "Long",
                 "Double",
                 "Float",
                 "Boolean",
                 "Byte",
                 "Short",
                 "Character",
                 "Object",
                 "Number",
                 "List",
                 "Set",
                 "Collection",
                 "Map",
                 "HashMap",
                 "HashSet",
                 "ArrayList",
                 "LinkedList",
                 "Optional",
                 "Iterable",
                 "Iterator" -> true;

            default -> false;
        };
    }

    /**
     * Prevent duplicate relationships.
     */
    private void addRelationshipIfNotExists(
            UmlClass umlClass,
            String targetType,
            RelationshipType relationshipType) {

        boolean exists = umlClass.getRelationships()
                .stream()
                .anyMatch(existing ->
                        existing.getSourceClassName()
                                .equals(umlClass.getName())
                                &&
                                existing.getTargetClassName()
                                        .equals(targetType)
                                &&
                                existing.getType() == relationshipType
                );

        if (!exists) {

            UmlRelationship relationship =
                    new UmlRelationship(
                            umlClass.getName(),
                            targetType,
                            relationshipType
                    );

            umlClass.addRelationship(relationship);
        }
    }

    private Visibility getVisibility(FieldDeclaration decl) {

        if (decl.isPublic()) return Visibility.PUBLIC;
        if (decl.isProtected()) return Visibility.PROTECTED;
        if (decl.isPrivate()) return Visibility.PRIVATE;

        return Visibility.PACKAGE;
    }

    private Visibility getVisibility(MethodDeclaration decl) {

        if (decl.isPublic()) return Visibility.PUBLIC;
        if (decl.isProtected()) return Visibility.PROTECTED;
        if (decl.isPrivate()) return Visibility.PRIVATE;

        return Visibility.PACKAGE;
    }
}