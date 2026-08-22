package com.deepak.uml.model;

/**
 * Represents a relationship between two UML classes.
 * Currently supports inheritance (extends) and implementation (implements).
 */
public class UmlRelationship {
    private String sourceClassName;    // The class that has the relationship
    private String targetClassName;    // The class/interface being related to
    private RelationshipType type;     // Type of relationship

    public UmlRelationship(String sourceClassName, String targetClassName, RelationshipType type) {
        this.sourceClassName = sourceClassName;
        this.targetClassName = targetClassName;
        this.type = type;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public RelationshipType getType() {
        return type;
    }

    @Override
    public String toString() {
        return sourceClassName + " " + type.getKeyword() + " " + targetClassName;
    }
}

