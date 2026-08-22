package com.deepak.uml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method in a UML class.
 */
public class UmlMethod {
    private String name;
    private String returnType;
    private Visibility visibility;
    private List<UmlParameter> parameters;

    public UmlMethod(String name, String returnType, Visibility visibility) {
        this.name = name;
        this.returnType = returnType;
        this.visibility = visibility;
        this.parameters = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public List<UmlParameter> getParameters() {
        return parameters;
    }

    public void addParameter(UmlParameter parameter) {
        parameters.add(parameter);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(visibility.getSymbol()).append(" ").append(name).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(parameters.get(i).getName()).append(" : ").append(parameters.get(i).getType());
        }
        sb.append(") : ").append(returnType);
        return sb.toString();
    }
}
