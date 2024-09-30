package ru.liga.packagesproject.services.truckloading.truckloadingstrategies;

import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.List;

public interface LoadingStrategy {

    List<Truck> loadPackages(List<Package> packages, List<Truck> trucks);

    /**
     * Сортируем посылки в порядке убывания площади, чтобы максимальные посылки были первыми.
     *
     * @param packages список посылок
     */
    default void sortPackagesByAreaInDescendingOrder(List<Package> packages) {
        packages.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }
}
