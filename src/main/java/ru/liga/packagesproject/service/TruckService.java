package ru.liga.packagesproject.service;

import ru.liga.packagesproject.model.Truck;
import ru.liga.packagesproject.model.TruckLoadingProcessSettings;

import java.util.List;

public interface TruckService {

    /**
     * Загружает посылки в грузовики на основе списка названий посылок.
     *
     * @param packageNames список названий посылок для загрузки
     * @param settings     параметры процесса загрузки траков
     * @return список загруженных грузовиков
     */
    List<Truck> loadPackagesToTrucks(String[] packageNames, TruckLoadingProcessSettings settings);

    /**
     * Загружает посылки в грузовики на основе данных из файла.
     *
     * @param filePath путь до файла с посылками для загрузки
     * @param settings параметры процесса загрузки траков
     * @return список загруженных грузовиков
     */
    List<Truck> loadPackagesToTrucks(String filePath, TruckLoadingProcessSettings settings);

    /**
     * Разгружает грузовики из файла JSON.
     *
     * @param filePath путь до JSON файла с информацией о грузовиках
     * @return список разгруженных грузовиков
     */
    List<Truck> unloadTrucksFromJsonFile(String filePath);

    /**
     * Сохраняет информацию о грузовиках в файл JSON.
     *
     * @param trucksForWriting    список грузовиков для записи
     * @param filePathDestination путь для сохранения JSON файла
     */
    void writeTrucksToJsonFile(List<Truck> trucksForWriting, String filePathDestination);

    /**
     * Печатает информацию о всех грузовиках на консоль.
     *
     * @param trucks список грузовиков
     */
    void printAllTrucks(List<Truck> trucks);
}
