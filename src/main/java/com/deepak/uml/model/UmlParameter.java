package com.deepak.uml.model;

/**
 * Represents a parameter in a method signature.
 */
public class UmlParameter {
    private String name;
    private String type;

    public UmlParameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
