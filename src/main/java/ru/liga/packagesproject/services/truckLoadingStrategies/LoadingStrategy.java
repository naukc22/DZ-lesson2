package ru.liga.packagesproject.services.truckLoadingStrategies;

import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.Comparator;
import java.util.List;

public interface LoadingStrategy {

    List<Truck> loadPackages(List<Package> packages, int truckCount, int truckHeight, int truckWidth);

    default void sortPackagesByArea(List<Package> packages) {
        packages.sort(Comparator.comparingInt(Package::getArea).reversed());
    }
}
