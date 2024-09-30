package ru.liga.packagesproject.services.truckloading.truckloadingstrategies;


import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.Comparator;
import java.util.List;

/**
 * BalancedLoadingStrategy реализует стратегию равномерной загрузки грузовиков.
 * Посылки распределяются по тракам с наименьшей текущей загрузкой.
 */
@Slf4j
public class BalancedLoadingStrategy implements LoadingStrategy {

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
                if (findSpaceForLoadingPackageIntoTruckAndTryToLoad(truck, currentPackage)) {
                    isPackageLoaded = true;
                    break;
                }
            }

            if (!isPackageLoaded) {
                throw new RuntimeException("Ошибка: Посылка '" + currentPackage.getName() + "' не поместилась в доступные грузовики.");
            }
        }

        return trucks;
    }

    private static void sortTruckByCurrentLoad(List<Truck> trucks) {
        trucks.sort(Comparator.comparingInt(Truck::getCurrentLoad));
    }

}


