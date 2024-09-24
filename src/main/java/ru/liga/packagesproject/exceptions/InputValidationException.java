package ru.liga.packagesproject.exceptions;

import java.util.List;

public class InputValidationException extends RuntimeException {

    public InputValidationException(List<String> errors) {
        super("Обнаружены ошибки валидации: " + String.join(", ", errors));
    }

}
