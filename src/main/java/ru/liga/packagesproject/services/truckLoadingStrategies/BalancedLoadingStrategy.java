package ru.liga.packagesproject.services.truckLoadingStrategies;


import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class BalancedLoadingStrategy implements LoadingStrategy {

    @Override
    public List<Truck> loadPackages(List<Package> packages, int truckCount, int truckHeight, int truckWidth) {

        List<Truck> trucks = createTruckListAccordingTruckCount(truckCount, truckHeight, truckWidth);
        sortPackagesByArea(packages);
        log.debug("Посылки отсортированы по площади: {}", packages);

        while (!packages.isEmpty()) {
            Truck truckWithLeastLoad = findTruckWithLeastLoad(trucks);
            log.debug("Выбран грузовик с наименьшей нагрузкой: {}", truckWithLeastLoad.getCurrentLoad());
            boolean isPackageLoaded = false;

            for (int row = truckHeight - 1; row >= 0 && !isPackageLoaded; row--) {
                for (int column = 0; column < truckWidth && !isPackageLoaded; column++) {

                    if (!truckWithLeastLoad.isCurrentCellOccupied(row, column)) {
                        isPackageLoaded = truckWithLeastLoad.tryLoadPackage(packages, row, column);
                    }
                }
            }

            if (!isPackageLoaded && !packages.isEmpty()) {
                throw new RuntimeException("Ошибка: Посылки не поместились в доступные грузовики.");
            }
        }

        return trucks;
    }

    private List<Truck> createTruckListAccordingTruckCount(int truckCount, int truckHeight, int truckWidth) {
        ArrayList<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            trucks.add(new Truck(truckHeight, truckWidth));
        }
        return trucks;
    }

    private Truck findTruckWithLeastLoad(List<Truck> trucks) {
        return trucks.stream()
                .min(Comparator.comparingInt(Truck::getCurrentLoad))
                .orElseThrow(() -> new RuntimeException("Нет доступных грузовиков для загрузки."));
    }

}


