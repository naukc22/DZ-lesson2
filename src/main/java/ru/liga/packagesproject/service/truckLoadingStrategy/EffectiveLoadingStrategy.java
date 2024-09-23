package ru.liga.packagesproject.service.truckLoadingStrategy;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.model.Truck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class EffectiveLoadingStrategy implements LoadingStrategy {

    int truckWidth;
    int truckHeight;
    List<Truck> trucks;

    public EffectiveLoadingStrategy(int truckWidth, int truckHeight) {
        this.truckWidth = truckWidth;
        this.truckHeight = truckHeight;
    }

    @Override
    public List<Truck> loadPackages(List<Package> packages, int truckCount) {
        trucks = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            trucks.add(createNewTruck());
        }

        sortPackagesInNumericalOrder(packages);
        log.debug("Посылки отсортированы по размеру: {}", packages);

        int currentTruckIndex = 0;
        Truck currentTruck = trucks.get(currentTruckIndex);

        for (int row = truckHeight - 1; row >= 0; row--) {
            for (int column = 1; column <= truckWidth; column++) {

                if (isTruckFull(row, column) && !packages.isEmpty()) {
                    currentTruckIndex++;
                    checkIfWeHaveOneMoreEmptyTruck(currentTruckIndex);
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

                if (!isLoaded && !packages.isEmpty() && isTruckFull(row, column)) {
                    currentTruckIndex++;
                    checkIfWeHaveOneMoreEmptyTruck(currentTruckIndex);
                    currentTruck = trucks.get(currentTruckIndex);
                    row = truckHeight - 1;
                    column = 0;
                }

            }
        }
        return trucks;
    }

    private void checkIfWeHaveOneMoreEmptyTruck(int currentTruckIndex) {
        if (currentTruckIndex >= trucks.size()) {
            log.error("Ошибка: Посылки не поместились во все доступные грузовики.");
            throw new RuntimeException("Ошибка: Посылки не поместились во все доступные грузовики.");
        }
    }

    private Truck createNewTruck() {
        return new Truck(truckHeight, truckWidth);
    }


    private boolean isTruckFull(int row, int col) {
        return col == truckWidth && row == 0;
    }


    private void sortPackagesInNumericalOrder(List<Package> packages) {
        packages.sort(Comparator.comparingInt(pack -> pack.getShape()[0][0]));
        Collections.reverse(packages);
    }


}
