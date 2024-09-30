package ru.liga.packagesproject.validators;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.exceptions.InputValidationException;
import ru.liga.packagesproject.controllers.LoadingMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для валидации входных данных, предоставленных пользователем,
 * таких как файл с посылками, путь для вывода, тип загрузки и количество траков.
 */
@Slf4j
public class InputDataValidator {

    /**
     * Валидирует путь к файлу с посылками, путь для вывода, тип загрузки и количество траков.
     * Если данные не валидны, выбрасывает {@link InputValidationException} с детализированным списком ошибок.
     *
     * @param filePath         путь к файлу с посылками.
     * @param filePathOutput   путь для записи результата.
     * @param loadingTypeInput строковое представление типа загрузки.
     * @param truckCount       количество траков для загрузки.
     * @throws InputValidationException если данные не валидны.
     */
    public void validateInputData(String filePath, String filePathOutput, String loadingTypeInput, int truckCount) {
        List<String> errors = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            errors.add("Ошибка: файл не найден.");
        }

        File fileDestination = new File(filePathOutput);
        if (!fileDestination.exists()) {
            errors.add("Ошибка: файл для записи не найден.");
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