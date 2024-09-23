package ru.liga.packagesproject.validator;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.exception.InputValidationException;
import ru.liga.packagesproject.model.LoadingMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InputDataValidator {

    public void validateInputData(String filePath, String loadingTypeInput, int truckCount) {
        List<String> errors = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            errors.add("Ошибка: файл не найден.");
        }

        try {
            LoadingMode.valueOf(loadingTypeInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            errors.add("Ошибка: неверный тип загрузки.");
        }

        if (truckCount <= 0) {
            errors.add("Ошибка: количество траков должно быть больше нуля.");
        }

        if (!errors.isEmpty()) {
            throw new InputValidationException(errors);
        }

        log.info("Все входные данные валидны.");
    }
}