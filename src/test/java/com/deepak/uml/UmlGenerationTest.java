package com.deepak.uml;

import com.deepak.uml.analyzer.ClassAnalyzer;
import com.deepak.uml.generator.MermaidGenerator;
import com.deepak.uml.generator.PlantUMLGenerator;
import com.deepak.uml.model.ClassType;
import com.deepak.uml.model.RelationshipType;
import com.deepak.uml.model.UmlClass;
import com.deepak.uml.parser.JavaSourceParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UmlGenerationTest {

    private final JavaSourceParser sourceParser = new JavaSourceParser();
    private final ClassAnalyzer classAnalyzer = new ClassAnalyzer();

    @Test
    void extractsClassMembersAndPackage() {
        CompilationUnit compilationUnit = sourceParser.parseString("""
                package example;

                public class Invoice {
                    private String number;

                    public double total(int quantity) {
                        return quantity;
                    }
                }
                """);

        List<UmlClass> umlClasses = classAnalyzer.analyzeCompilationUnit(compilationUnit);

        assertEquals(1, umlClasses.size());
        UmlClass invoice = umlClasses.get(0);
        assertEquals("Invoice", invoice.getName());
        assertEquals("example", invoice.getPackageName());
        assertEquals(ClassType.CLASS, invoice.getType());
        assertEquals(1, invoice.getFields().size());
        assertEquals(1, invoice.getMethods().size());
        assertEquals("number", invoice.getFields().get(0).getName());
        assertEquals("total", invoice.getMethods().get(0).getName());
    }

    @Test
    void detectsInheritanceImplementationAndProjectTypeRelationships() {
        CompilationUnit baseUnit = sourceParser.parseString("""
                class BaseProcessor { }
                """);
        CompilationUnit gatewayUnit = sourceParser.parseString("""
                interface Gateway { void process(); }
                """);
        CompilationUnit paymentUnit = sourceParser.parseString("""
                class Payment extends BaseProcessor implements Gateway {
                    private BaseProcessor processor;

                    public void process() { }
                }
                """);

        List<UmlClass> umlClasses = classAnalyzer.analyzeCompilationUnits(
                List.of(baseUnit, gatewayUnit, paymentUnit));
        UmlClass payment = umlClasses.stream()
                .filter(umlClass -> "Payment".equals(umlClass.getName()))
                .findFirst()
                .orElseThrow();

        assertTrue(payment.getRelationships().stream().anyMatch(relationship ->
                relationship.getType() == RelationshipType.INHERITANCE
                        && "BaseProcessor".equals(relationship.getTargetClassName())));
        assertTrue(payment.getRelationships().stream().anyMatch(relationship ->
                relationship.getType() == RelationshipType.IMPLEMENTATION
                        && "Gateway".equals(relationship.getTargetClassName())));
        assertTrue(payment.getRelationships().stream().anyMatch(relationship ->
                relationship.getType() == RelationshipType.ASSOCIATION
                        && "BaseProcessor".equals(relationship.getTargetClassName())));
    }

    @Test
    void generatesMermaidAndPlantUmlRelationships() {
        CompilationUnit compilationUnit = sourceParser.parseString("""
                interface Gateway { void process(); }
                class Payment implements Gateway {
                    public void process() { }
                }
                """);
        List<UmlClass> umlClasses = classAnalyzer.analyzeCompilationUnit(compilationUnit);

        String mermaid = new MermaidGenerator().generate(umlClasses);
        String plantUml = new PlantUMLGenerator().generate(umlClasses);

        assertTrue(mermaid.startsWith("classDiagram"));
        assertTrue(mermaid.contains("Payment ..|> Gateway : implements"));
        assertTrue(plantUml.startsWith("@startuml"));
        assertTrue(plantUml.contains("Payment ..|> Gateway"));
        assertTrue(plantUml.endsWith("@enduml\n"));
    }
}