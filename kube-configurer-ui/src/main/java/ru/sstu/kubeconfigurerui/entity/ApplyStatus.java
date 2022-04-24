package ru.sstu.kubeconfigurerui.entity;

public enum ApplyStatus {
    SUCCESS("Успешно"),
    IN_PROGRESS("Применяется"),
    FAILED("Ошибка");

    public final String message;

    ApplyStatus(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}


