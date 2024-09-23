package ru.liga.packagesproject.service.truckLoadingStrategy;

import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.model.Truck;

import java.util.Comparator;
import java.util.List;

public interface LoadingStrategy {

    List<Truck> loadPackages(List<Package> packages, int truckCount);

    default void sortPackagesByArea(List<Package> packages) {
        packages.sort(Comparator.comparingInt(Package::getArea).reversed());
    }
}
