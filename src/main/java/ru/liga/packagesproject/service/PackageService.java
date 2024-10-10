package ru.liga.packagesproject.service;

import ru.liga.packagesproject.dto.PackageDto;
import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.model.Package;

import java.util.List;
import java.util.Optional;

public interface PackageService {

    /**
     * Возвращает все посылки.
     *
     * @return итерабельный список посылок
     */
    List<PackageDto> findAll();

    /**
     * Создает новую посылку с заданным именем, символом и формой.
     *
     * @param name   имя посылки
     * @param symbol символ, представляющий посылку
     * @param form   форма посылки в виде списка строк
     * @return созданная посылка
     */
    Package create(String name, char symbol, List<String> form) throws PackageAlreadyExistsException;

    /**
     * Ищет посылку по имени.
     *
     * @param packageName имя посылки
     * @return опциональный объект с найденной посылкой или пустой, если посылка не найдена
     */
    Optional<Package> findByName(String packageName);

    /**
     * Ищет посылку по форме.
     *
     * @param packageForm форма посылки
     * @return опциональный объект с найденной посылкой или пустой, если посылка не найдена
     */
    Optional<Package> findByForm(List<String> packageForm);

    /**
     * Обновляет существующую посылку с заданным именем.
     *
     * @param name   имя посылки, которую нужно обновить
     * @param symbol новый символ посылки
     * @param form   новая форма посылки в виде списка строк
     */
    void update(String name, char symbol, List<String> form);

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки, которую нужно удалить
     */
    void remove(String name);

    /**
     * Найти посылку по символу
     */
    Iterable<Package> findBySymbol(char symbol);

}

