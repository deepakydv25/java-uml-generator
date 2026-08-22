package com.deepak.uml.model;

/**
 * Represents the visibility level (access modifier) of a class member.
 */
public enum Visibility {
    PUBLIC("+"),
    PROTECTED("#"),
    PRIVATE("-"),
    PACKAGE("~");

    private final String symbol;

    Visibility(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
