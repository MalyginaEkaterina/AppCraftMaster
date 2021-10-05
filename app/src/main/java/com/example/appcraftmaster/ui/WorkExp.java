package com.example.appcraftmaster.ui;

import org.jetbrains.annotations.NotNull;

public enum WorkExp {
    NONE(0, "Нет опыта"),
    YEAR_1(1, "1 год"),
    YEAR_1_3(2, "от 1 до 3х лет"),
    YEAR_3_6(3, "от 3х до 6ти лет"),
    YEAR_6(4, "более 6ти лет");

    private final int id;

    private final String text;

    WorkExp(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    @NotNull
    @Override
    public String toString() {
        return text;
    }

    public static String getTextById(Integer id) {
        switch (id) {
            case (0):
                return NONE.toString();
            case (1):
                return YEAR_1.toString();
            case (2):
                return YEAR_1_3.toString();
            case (3):
                return YEAR_3_6.toString();
            case (4):
                return YEAR_6.toString();
            default:
                return null;
        }
    }
}
