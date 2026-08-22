package com.deepak.uml.generator;

import com.deepak.uml.model.*;
import java.util.List;

/**
 * Generates Mermaid class diagram syntax from UML classes.
 * Output can be used in GitHub README, documentation, etc.
 *
 * Example output:
 * classDiagram
 *     class Order {
 *         - gateway: PaymentGateway
 *         + checkout() void
 *     }
 */
public class MermaidGenerator implements DiagramGenerator {

    /**
     * Generates a complete Mermaid class diagram from multiple UML classes.
     *
     * @param umlClasses list of UML classes to diagram
     * @return Mermaid class diagram syntax as String
     */
    @Override
    public String generate(List<UmlClass> umlClasses) {
        StringBuilder sb = new StringBuilder();
        sb.append("classDiagram\n");

        // Generate class definitions
        for (UmlClass umlClass : umlClasses) {
            generateClassDefinition(umlClass, sb);
        }

        // Generate relationships
        for (UmlClass umlClass : umlClasses) {
            generateRelationships(umlClass, sb);
        }

        return sb.toString();
    }

    /**
     * Generates a single class definition.
     */
    private void generateClassDefinition(UmlClass umlClass, StringBuilder sb) {
        sb.append("    class ").append(umlClass.getName()).append(" {\n");

        // Add class type notation (interface, abstract, etc.)
        if (umlClass.getType() == ClassType.INTERFACE) {
            sb.append("        <<interface>>\n");
        } else if (umlClass.getType() == ClassType.ENUM) {
            sb.append("        <<enumeration>>\n");
        } else if (umlClass.getType() == ClassType.ANNOTATION) {
            sb.append("        <<annotation>>\n");
        }

        // Add fields
        for (UmlField field : umlClass.getFields()) {
            sb.append("        ").append(field.getVisibility().getSymbol()).append(" ");
            sb.append(field.getName()).append(": ").append(field.getType()).append("\n");
        }

        // Add methods
        for (UmlMethod method : umlClass.getMethods()) {
            sb.append("        ").append(method.getVisibility().getSymbol()).append(" ");
            sb.append(method.getName()).append("(");

            // Add parameters
            List<UmlParameter> params = method.getParameters();
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(params.get(i).getType());
            }

            sb.append(") ").append(method.getReturnType()).append("\n");
        }

        sb.append("    }\n");
    }

    /**
     * Generates relationship lines for inheritance and implementation.
     */
    private void generateRelationships(
            UmlClass umlClass,
            StringBuilder sb) {

        for (UmlRelationship rel : umlClass.getRelationships()) {

            if (rel.getType() == RelationshipType.INHERITANCE) {

                sb.append("    ")
                        .append(rel.getSourceClassName())
                        .append(" --|> ")
                        .append(rel.getTargetClassName())
                        .append(" : extends\n");

            } else if (rel.getType() == RelationshipType.IMPLEMENTATION) {

                sb.append("    ")
                        .append(rel.getSourceClassName())
                        .append(" ..|> ")
                        .append(rel.getTargetClassName())
                        .append(" : implements\n");

            } else if (rel.getType() == RelationshipType.ASSOCIATION) {

                sb.append("    ")
                        .append(rel.getSourceClassName())
                        .append(" --> ")
                        .append(rel.getTargetClassName())
                        .append(" : association\n");

            } else if (rel.getType() == RelationshipType.DEPENDENCY) {

                sb.append("    ")
                        .append(rel.getSourceClassName())
                        .append(" ..> ")
                        .append(rel.getTargetClassName())
                        .append(" : uses\n");
            }
        }
    }
}
