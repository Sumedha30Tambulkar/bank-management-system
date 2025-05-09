package com.example.main.entity;

public enum Gender {
    MALE("M", "Male"),
    FEMALE("F", "Female"),
    OTHER("O", "Other");

    private final String code;
    private final String label;

    Gender(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    // Optional: Find by code
    public static Gender fromCode(String code) {
        for (Gender gender : Gender.values()) {
            if (gender.code.equalsIgnoreCase(code)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender code: " + code);
    }
}