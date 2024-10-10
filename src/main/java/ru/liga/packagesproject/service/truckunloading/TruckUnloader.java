package ru.liga.packagesproject.service.truckunloading;

import ru.liga.packagesproject.dto.Truck;
import ru.liga.packagesproject.dto.TruckBodyDto;

/**
 * Класс для разгрузки траков.
 */
public interface TruckUnloader {

    /**
     * Парсит truckBody. Определяет какие посылки там загружены.
     *
     * @param truckBody DTO объект представляющий тело трака с посылками
     * @return объект Truck в котором загружены посылки в той же последовательности как и были во входящих данных.
     */
    Truck unloadTruck(TruckBodyDto truckBody);

}
