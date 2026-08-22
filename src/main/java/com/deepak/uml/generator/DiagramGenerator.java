package com.deepak.uml.generator;

import com.deepak.uml.model.UmlClass;
import java.util.List;

/**
 * Interface for diagram generators.
 * Implementations convert UML model to various diagram formats.
 */
public interface DiagramGenerator {
    /**
     * Generates a diagram from UML classes.
     *
     * @param umlClasses list of UML classes to diagram
     * @return diagram syntax as String
     */
    String generate(List<UmlClass> umlClasses);
}
