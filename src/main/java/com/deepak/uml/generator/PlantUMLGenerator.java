package com.deepak.uml.generator;

import com.deepak.uml.model.*;
import java.util.List;

/**
 * Generates PlantUML class diagram syntax from UML classes.
 * Output can be used in various documentation platforms.
 *
 * Example output:
 * @startuml
 * class Order {
 *     - gateway: PaymentGateway
 *     + checkout(): void
 * }
 * @enduml
 */
public class PlantUMLGenerator implements DiagramGenerator {

    /**
     * Generates a complete PlantUML class diagram from multiple UML classes.
     *
     * @param umlClasses list of UML classes to diagram
     * @return PlantUML class diagram syntax as String
     */
    @Override
    public String generate(List<UmlClass> umlClasses) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("!theme plain\n");
        sb.append("skinparam classBackgroundColor #FFFFFF\n");
        sb.append("skinparam classBorderColor #000000\n\n");

        // Generate class definitions
        for (UmlClass umlClass : umlClasses) {
            generateClassDefinition(umlClass, sb);
        }

        // Generate relationships
        for (UmlClass umlClass : umlClasses) {
            generateRelationships(umlClass, sb);
        }

        sb.append("\n@enduml\n");
        return sb.toString();
    }

    /**
     * Generates a single class definition.
     */
    private void generateClassDefinition(UmlClass umlClass, StringBuilder sb) {
        // Class declaration with type
        if (umlClass.getType() == ClassType.INTERFACE) {
            sb.append("interface ");
        } else if (umlClass.getType() == ClassType.ENUM) {
            sb.append("enum ");
        } else if (umlClass.getType() == ClassType.ANNOTATION) {
            sb.append("annotation ");
        } else {
            sb.append("class ");
        }

        sb.append(umlClass.getName()).append(" {\n");

        // Add fields
        for (UmlField field : umlClass.getFields()) {
            sb.append("    ").append(field.getVisibility().getSymbol()).append(" ");
            sb.append(field.getName()).append(": ").append(field.getType()).append("\n");
        }

        // Add separator if both fields and methods exist
        if (!umlClass.getFields().isEmpty() && !umlClass.getMethods().isEmpty()) {
            sb.append("    --\n");
        }

        // Add methods
        for (UmlMethod method : umlClass.getMethods()) {
            sb.append("    ").append(method.getVisibility().getSymbol()).append(" ");
            sb.append(method.getName()).append("(");

            // Add parameters
            List<UmlParameter> params = method.getParameters();
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(params.get(i).getName()).append(": ").append(params.get(i).getType());
            }

            sb.append("): ").append(method.getReturnType()).append("\n");
        }

        sb.append("}\n\n");
    }

    /**
     * Generates relationship lines for inheritance and implementation.
     */
    private void generateRelationships(UmlClass umlClass, StringBuilder sb) {
        for (UmlRelationship rel : umlClass.getRelationships()) {
            if (rel.getType() == RelationshipType.INHERITANCE) {
                // Inheritance: solid arrow pointing to parent
                sb.append(rel.getSourceClassName())
                        .append(" --|> ").append(rel.getTargetClassName())
                        .append("\n");
            } else if (rel.getType() == RelationshipType.IMPLEMENTATION) {
                // Implementation: dashed arrow pointing to interface
                sb.append(rel.getSourceClassName())
                        .append(" ..|> ").append(rel.getTargetClassName())
                        .append("\n");
            }
        }
    }
}
