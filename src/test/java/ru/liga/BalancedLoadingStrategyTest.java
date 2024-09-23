package ru.liga;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.packagesproject.model.Package;
import ru.liga.packagesproject.model.Truck;
import ru.liga.packagesproject.service.truckLoadingStrategy.BalancedLoadingStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BalancedLoadingStrategyTest {

    private BalancedLoadingStrategy balancedLoadingStrategy;

    @BeforeEach
    void setUp() {
        balancedLoadingStrategy = new BalancedLoadingStrategy(6, 6);
    }

    @Test
    void testLoadPackagesSuccessfully() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1}, {1, 1}}));
        packages.add(new Package(new int[][]{{1, 1, 1}}));

        List<Truck> trucks = balancedLoadingStrategy.loadPackages(packages, 1);

        assertEquals(1, trucks.size());
        Truck truck = trucks.get(0);

        assertEquals(7, truck.getCurrentLoad());
    }

    @Test
    void testLoadPackagesMultipleTrucks() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1, 1, 1, 1, 1, 1}}));
        packages.add(new Package(new int[][]{
                {1, 1},
                {1, 1},
                {1, 1}}
        ));

        List<Truck> trucks = balancedLoadingStrategy.loadPackages(packages, 2);

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

        assertThrows(RuntimeException.class, () -> balancedLoadingStrategy.loadPackages(packages, 1));
    }

    @Test
    void testLoadMultiplePackagesWithSorting() {
        List<Package> packages = new ArrayList<>();
        packages.add(new Package(new int[][]{{1}}));  // Посылка 1x1
        packages.add(new Package(new int[][]{
                {1, 1},
                {1, 1}}
        ));
        packages.add(new Package(new int[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}}
        ));

        List<Truck> trucks = balancedLoadingStrategy.loadPackages(packages, 1);

        assertEquals(1, trucks.size());
        Truck truck = trucks.get(0);

        assertEquals(14, truck.getCurrentLoad());
    }
}

