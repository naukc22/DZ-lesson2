package ru.liga.packagesproject.service.truckLoadingStrategies;


import lombok.extern.log4j.Log4j2;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.model.Truck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log4j2
public class BalancedLoadingStrategy implements LoadingStrategy {

    int truckWidth;
    int truckHeight;
    List<Truck> trucks;

    public BalancedLoadingStrategy(int truckWidth, int truckHeight) {
        this.truckWidth = truckWidth;
        this.truckHeight = truckHeight;
    }


    @Override
    public List<Truck> loadPackages(List<Package> packages, int truckCount) {
        List<Truck> trucks = createTruckListAccordingTruckCount(truckCount);
        sortPackagesByArea(packages);
        log.debug("Посылки отсортированы по площади: {}", packages);

        while (!packages.isEmpty()) {
            Truck truckWithLeastLoad = findTruckWithLeastLoad(trucks);
            log.debug("Выбран грузовик с наименьшей нагрузкой: {}", truckWithLeastLoad.getCurrentLoad());
            boolean isPackageLoaded = false;

            for (int row = truckHeight - 1; row >= 0 && !isPackageLoaded; row--) {
                for (int column = 1; column <= truckWidth && !isPackageLoaded; column++) {
                    if (!truckWithLeastLoad.isCurrentCellOccupied(row, column)) {
                        isPackageLoaded = truckWithLeastLoad.tryLoadPackage(packages, row, column);
                    }
                }
            }

            if (!isPackageLoaded && !packages.isEmpty()) {
                log.error("Ошибка: Посылки не поместились в доступные грузовики.");
                throw new RuntimeException("Ошибка: Посылки не поместились в доступные грузовики.");
            }
        }

        return trucks;
    }

    private List<Truck> createTruckListAccordingTruckCount(int truckCount) {
        trucks = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            trucks.add(createNewTruck());
        }
        return trucks;
    }

    private Truck findTruckWithLeastLoad(List<Truck> trucks) {
        return trucks.stream()
                .min(Comparator.comparingInt(Truck::getCurrentLoad))
                .orElseThrow(() -> {
                    log.error("Нет доступных грузовиков для загрузки.");
                    return new RuntimeException("Нет доступных грузовиков для загрузки.");
                });
    }

    private Truck createNewTruck() {
        return new Truck(truckHeight, truckWidth);
    }

}


