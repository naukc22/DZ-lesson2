package ru.liga.packagesproject.service;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.model.LoadingMode;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.model.Truck;
import ru.liga.packagesproject.service.truckLoadingStrategy.LoadingStrategy;
import ru.liga.packagesproject.service.truckLoadingStrategy.LoadingStrategyFactory;
import ru.liga.packagesproject.util.PackageCounter;
import ru.liga.packagesproject.validator.PackageValidator;

import java.util.List;
import java.util.Map;

@Slf4j
public class TruckService {

    private static final int TRUCK_WIDTH = 6;
    private static final int TRUCK_HEIGHT = 6;

    public List<Truck> loadPackages(List<Package> packages, LoadingMode loadingMode, int truckCount) {
        log.info("Начинаем загрузку посылок. Режим загрузки: {}, Количество грузовиков: {}", loadingMode, truckCount);

        PackageValidator.sortValidPackages(packages, TRUCK_HEIGHT, TRUCK_WIDTH);

        LoadingStrategy strategy = LoadingStrategyFactory.getStrategyFromLoadingMode(loadingMode, TRUCK_WIDTH, TRUCK_HEIGHT);
        List<Truck> trucks = strategy.loadPackages(packages, truckCount);

        log.info("Загрузка посылок завершена. Загружено грузовиков: {}", trucks.size());
        return trucks;
    }

    public void printAllTrucks(List<Truck> trucks) {
        int truckNumber = 1;
        for (Truck truck : trucks) {
            log.info("Печать информации о грузовике {}", truckNumber);
            System.out.println("Truck " + truckNumber + ":");
            truck.printTruck();
            truckNumber++;
        }
    }

    public void countAndPrintPackagesFromTruckList(List<Truck> trucks) {
        for (int i = 0; i < trucks.size(); i++) {
            Truck truck = trucks.get(i);
            log.info("Обработка грузовика {}", (i + 1));
            System.out.println("Трак " + (i + 1) + ":");
            truck.printTruck();

            // Считаем посылки
            Map<Character, Integer> packageCounts = PackageCounter.countPackages(truck);
            log.debug("Результат подсчета посылок для грузовика {}: {}", (i + 1), packageCounts);
            System.out.println("Результат подсчета посылок:");
            PackageCounter.printPackageCounts(packageCounts);
            System.out.println();
        }
    }

}
