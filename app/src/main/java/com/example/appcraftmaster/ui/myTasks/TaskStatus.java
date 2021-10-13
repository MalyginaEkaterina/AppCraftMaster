package com.example.appcraftmaster.ui.myTasks;

import org.jetbrains.annotations.NotNull;

public enum TaskStatus {
    NEW(1, "Создано"),
    DONE(2, "Выполнено"),
    CLOSED(3, "Закрыто"),
    ASSIGNED(4, "Назначено");

    private final int id;

    private final String text;

    TaskStatus(int id, String text) {
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
            case (1):
                return NEW.toString();
            case (2):
                return DONE.toString();
            case (3):
                return CLOSED.toString();
            case (4):
                return ASSIGNED.toString();
            default:
                return null;
        }
    }
}
