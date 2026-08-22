package com.deepak.uml.model;

/**
 * Represents a field (member variable) in a UML class.
 */
public class UmlField {
    private String name;
    private String type;
    private Visibility visibility;

    public UmlField(String name, String type, Visibility visibility) {
        this.name = name;
        this.type = type;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public String toString() {
        return visibility.getSymbol() + " " + name + " : " + type;
    }
}
