package ru.liga.packagesproject.services.truckLoadingStrategies;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * EffectiveLoadingStrategy реализует стратегию эффективной загрузки грузовиков.
 * Посылки загружаются как можно плотнее, занимая наименьшее возможное количество грузовиков.
 */
@Slf4j
public class EffectiveLoadingStrategy implements LoadingStrategy {

    /**
     * Метод реализует стратегию эффективной загрузки посылок в грузовики.
     *
     * @param packages    список посылок, которые нужно загрузить
     * @param truckCount  количество грузовиков
     * @param truckHeight высота грузовиков
     * @param truckWidth  ширина грузовиков
     * @return список грузовиков с загруженными посылками
     */
    @Override
    public List<Truck> loadPackages(List<Package> packages, int truckCount, int truckHeight, int truckWidth) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            trucks.add(new Truck(truckHeight, truckWidth));
        }

        sortPackagesInNumericalOrder(packages);
        log.debug("Посылки отсортированы по размеру: {}", packages);

        int currentTruckIndex = 0;
        Truck currentTruck = trucks.get(currentTruckIndex);

        for (int row = truckHeight - 1; row >= 0; row--) {
            for (int column = 0; column < truckWidth; column++) {

                if (isTruckFull(row, column, truckWidth) && !packages.isEmpty()) {
                    currentTruckIndex++;
                    checkIfWeHaveOneMoreEmptyTruck(trucks, currentTruckIndex);
                    currentTruck = trucks.get(currentTruckIndex);
                    row = truckHeight - 1;
                    column = 0;
                }

                boolean isLoaded = false;
                if (!currentTruck.isCurrentCellOccupied(row, column)) {
                    isLoaded = currentTruck.tryLoadPackage(packages, row, column);
                }

                if (isLoaded) {
                    log.debug("Посылка загружена в грузовик {} на позицию ({}, {})", currentTruckIndex + 1, row, column);
                }

                if (!isLoaded && !packages.isEmpty() && isTruckFull(row, column, truckWidth)) {
                    currentTruckIndex++;
                    checkIfWeHaveOneMoreEmptyTruck(trucks, currentTruckIndex);
                    currentTruck = trucks.get(currentTruckIndex);
                    row = truckHeight - 1;
                    column = 0;
                }

            }
        }
        return trucks;
    }

    private void checkIfWeHaveOneMoreEmptyTruck(List<Truck> trucks, int currentTruckIndex) {
        if (currentTruckIndex >= trucks.size()) {
            log.error("Ошибка: Посылки не поместились во все доступные грузовики.");
            throw new RuntimeException("Ошибка: Посылки не поместились во все доступные грузовики.");
        }
    }

    private boolean isTruckFull(int row, int col, int truckWidth) {
        return col == truckWidth - 1 && row == 0;
    }


    private void sortPackagesInNumericalOrder(List<Package> packages) {
        packages.sort(Comparator.comparingInt(pack -> pack.getShape()[0][0]));
        Collections.reverse(packages);
    }


}
