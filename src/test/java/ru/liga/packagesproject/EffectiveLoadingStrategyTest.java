package ru.liga.packagesproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.services.truckLoadingStrategies.EffectiveLoadingStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EffectiveLoadingStrategyTest {

    private EffectiveLoadingStrategy effectiveLoadingStrategy;

    @BeforeEach
    void setUp() {
        effectiveLoadingStrategy = new EffectiveLoadingStrategy();
    }

    @Test
    void testLoadPackagesSuccessfully() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1}, {1, 1}}));
        packages.add(new Package(new int[][]{{1, 1, 1}}));

        List<Truck> trucks = effectiveLoadingStrategy.loadPackages(packages, 1, 6, 6);

        assertEquals(1, trucks.size());
        Truck truck = trucks.get(0);

        assertEquals(7, truck.getCurrentLoad());
    }

    @Test
    void testLoadPackagesMultipleTrucks() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1, 1, 1, 1, 1}}));
        packages.add(new Package(new int[][]{{1, 1}, {1, 1}, {1, 1}}));

        List<Truck> trucks = effectiveLoadingStrategy.loadPackages(packages, 2, 6, 6);

        assertEquals(2, trucks.size());

        Truck truck1 = trucks.get(0);
        Truck truck2 = trucks.get(1);

        assertEquals(6, truck1.getCurrentLoad());
        assertEquals(6, truck2.getCurrentLoad());
    }

    @Test
    void testLoadPackagesErrorWhenNotEnoughSpace() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1, 1, 1, 1, 1, 1}}));

        assertThrows(RuntimeException.class, () -> effectiveLoadingStrategy.loadPackages(packages, 1, 6, 6));
    }

    @Test
    void testLoadMultiplePackagesWithSorting() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1}}));  // Посылка 1x1
        packages.add(new Package(new int[][]{{1, 1}, {1, 1}}));  // Посылка 2x2
        packages.add(new Package(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}));  // Посылка 3x3

        List<Truck> trucks = effectiveLoadingStrategy.loadPackages(packages, 1, 6, 6);

        assertEquals(1, trucks.size());
        Truck truck = trucks.get(0);

        assertEquals(14, truck.getCurrentLoad()); // 1 + 4 + 9 = 14 ячеек
    }

}

