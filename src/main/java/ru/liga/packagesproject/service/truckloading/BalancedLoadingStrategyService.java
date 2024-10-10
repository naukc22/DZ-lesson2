package ru.liga.packagesproject.service.truckloading;


import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.dto.Truck;
import ru.liga.packagesproject.exception.PackagesDidNotFitInTrucksException;
import ru.liga.packagesproject.model.Package;

import java.util.Comparator;
import java.util.List;

/**
 * BalancedLoadingStrategy реализует стратегию равномерной загрузки грузовиков.
 * Посылки распределяются по тракам с наименьшей текущей загрузкой.
 */
@Slf4j
public class BalancedLoadingStrategyService extends LoadingStrategyService {

    /**
     * Метод реализует стратегию загрузки посылок в грузовики.
     * Посылки сортируются по площади, затем распределяются по грузовикам с наименьшей загрузкой.
     *
     * @param packages список посылок, которые нужно загрузить
     * @param trucks   список грузовиков, которые нужно загрузить
     * @return список грузовиков с загруженными посылками
     */
    @Override
    public List<Truck> loadPackages(List<Package> packages, List<Truck> trucks) {

        sortPackagesByAreaInDescendingOrder(packages);
        log.debug("Посылки отсортированы по площади: {}", packages);

        for (Package currentPackage : packages) {
            boolean isPackageLoaded = false;

            sortTruckByCurrentLoad(trucks);

            for (Truck truck : trucks) {
                if (findSpaceAndTryLoad(truck, currentPackage)) {
                    isPackageLoaded = true;
                    break;
                }
            }

            if (!isPackageLoaded) {
                throw new PackagesDidNotFitInTrucksException(List.of(currentPackage.getName()));
            }
        }

        return trucks;
    }

    private static void sortTruckByCurrentLoad(List<Truck> trucks) {
        trucks.sort(Comparator.comparingInt(Truck::getCurrentLoad));
    }

}


