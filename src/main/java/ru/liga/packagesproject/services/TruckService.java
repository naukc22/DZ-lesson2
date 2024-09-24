package ru.liga.packagesproject.services;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.LoadingMode;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.services.truckLoadingStrategies.LoadingStrategy;
import ru.liga.packagesproject.services.truckLoadingStrategies.LoadingStrategyFactory;
import ru.liga.packagesproject.validators.PackageValidator;

import java.util.List;
import java.util.Map;

@Slf4j
public class TruckService {

    private static final int TRUCK_WIDTH = 6;
    private static final int TRUCK_HEIGHT = 6;

    public List<Truck> loadPackages(List<Package> packages, LoadingMode loadingMode, int truckCount) {
        log.info("Начинаем загрузку посылок. Режим загрузки: {}, Количество грузовиков: {}", loadingMode, truckCount);

        PackageValidator.sortValidPackages(packages, TRUCK_HEIGHT, TRUCK_WIDTH);

        LoadingStrategy strategy = LoadingStrategyFactory.getStrategyFromLoadingMode(loadingMode);
        List<Truck> trucks = strategy.loadPackages(packages, truckCount, TRUCK_HEIGHT, TRUCK_WIDTH);

        log.info("Загрузка посылок завершена. Загружено грузовиков: {}", trucks.size());
        return trucks;
    }

    public void printAllTrucks(List<Truck> trucks) {
        int truckNumber = 1;
        for (Truck truck : trucks) {
            log.info("Печать информации о грузовике {}", truckNumber);
            System.out.println("Truck " + truckNumber + ":");
            truck.printTruckToConsole();
            truckNumber++;
        }
    }

    public void countAndPrintPackagesFromTruckList(List<Truck> trucks) {
        for (int i = 0; i < trucks.size(); i++) {
            Truck truck = trucks.get(i);
            log.info("Обработка грузовика {}", (i + 1));
            System.out.println("Трак " + (i + 1) + ":");
            truck.printTruckToConsole();

            Map<Character, Integer> packageCounts = PackageCounterService.countPackages(truck);
            log.debug("Результат подсчета посылок для грузовика {}: {}", (i + 1), packageCounts);
            System.out.println("Результат подсчета посылок:");
            PackageCounterService.printPackageCounts(packageCounts);
            System.out.println();
        }
    }

}
