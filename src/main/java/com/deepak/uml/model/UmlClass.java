package com.deepak.uml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a UML class extracted from Java source code.
 */
public class UmlClass {
    private String name;
    private String packageName;
    private ClassType type;
    private List<UmlField> fields;
    private List<UmlMethod> methods;

    public UmlClass(String name, String packageName, ClassType type) {
        this.name = name;
        this.packageName = packageName;
        this.type = type;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public ClassType getType() {
        return type;
    }

    public List<UmlField> getFields() {
        return fields;
    }

    public void addField(UmlField field) {
        fields.add(field);
    }

    public List<UmlMethod> getMethods() {
        return methods;
    }

    public void addMethod(UmlMethod method) {
        methods.add(method);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(name).append("\n");
        sb.append("Package: ").append(packageName).append("\n");
        sb.append("Type: ").append(type).append("\n\n");

        if (!fields.isEmpty()) {
            sb.append("Fields:\n");
            for (UmlField field : fields) {
                sb.append("  ").append(field).append("\n");
            }
            sb.append("\n");
        }

        if (!methods.isEmpty()) {
            sb.append("Methods:\n");
            for (UmlMethod method : methods) {
                sb.append("  ").append(method).append("\n");
            }
        }

        return sb.toString();
    }
}
