package ru.liga.packagesproject.validator;

import lombok.extern.log4j.Log4j2;
import ru.liga.packagesproject.model.LoadingMode;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class InputDataValidator {

    public List<String> validateInputData(String filePath, String loadingTypeInput, int truckCount) {
        List<String> errors = new ArrayList<>();

        if (filePath == null || filePath.trim().isEmpty()) {
            errors.add("Ошибка: путь к файлу не может быть пустым.");
        }

        try {
            LoadingMode.valueOf(loadingTypeInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            errors.add("Ошибка: неверный тип загрузки. Введите SINGLE, EFFECTIVE или BALANCED.");
        }

        if (truckCount <= 0) {
            errors.add("Ошибка: количество траков должно быть больше нуля.");
        }

        if (errors.isEmpty()) {
            log.info("Все входные данные валидны.");
        } else {
            log.warn("Обнаружены ошибки валидации: {}", errors);
        }

        return errors;
    }

}