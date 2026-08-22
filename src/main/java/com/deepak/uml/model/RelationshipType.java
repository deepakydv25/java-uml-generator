package com.deepak.uml.model;

/**
 * Represents the type of relationship between UML classes.
 */
public enum RelationshipType {
    // Inheritance relationships
    INHERITANCE("extends"),      // Class A extends Class B
    IMPLEMENTATION("implements"), // Class A implements Interface B
    ASSOCIATION("association"),
    DEPENDENCY("uses");

    private final String keyword;

    RelationshipType(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}

