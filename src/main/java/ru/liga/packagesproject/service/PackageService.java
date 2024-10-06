package ru.liga.packagesproject.service;

import ru.liga.packagesproject.exception.PackageAlreadyExistsException;
import ru.liga.packagesproject.models.Package;

import java.util.List;
import java.util.Optional;

public interface PackageService {

    /**
     * Возвращает все посылки.
     *
     * @return итерабельный список посылок
     */
    Iterable<Package> findAllPackages();

    /**
     * Создает новую посылку с заданным именем, символом и формой.
     *
     * @param name   имя посылки
     * @param symbol символ, представляющий посылку
     * @param form   форма посылки в виде списка строк
     * @return созданная посылка
     */
    Package createPackage(String name, char symbol, List<String> form) throws PackageAlreadyExistsException;

    /**
     * Ищет посылку по имени.
     *
     * @param packageName имя посылки
     * @return опциональный объект с найденной посылкой или пустой, если посылка не найдена
     */
    Optional<Package> findPackageByName(String packageName);

    /**
     * Ищет посылку по форме.
     *
     * @param packageForm форма посылки
     * @return опциональный объект с найденной посылкой или пустой, если посылка не найдена
     */
    Optional<Package> findPackageByForm(List<String> packageForm);

    /**
     * Обновляет существующую посылку с заданным именем.
     *
     * @param name   имя посылки, которую нужно обновить
     * @param symbol новый символ посылки
     * @param form   новая форма посылки в виде списка строк
     */
    void updatePackage(String name, char symbol, List<String> form);

    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки, которую нужно удалить
     */
    void deletePackage(String name);
}

